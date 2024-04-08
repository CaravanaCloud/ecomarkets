import os
import subprocess
import sys
import json
import psycopg2
from psycopg2 import sql
import boto3
from botocore.exceptions import ClientError

# Function to get environment variables
def get_env(var_name):
    try:
        return os.environ[var_name]
    except KeyError:
        raise Exception(f"Environment variable {var_name} not found.")

def get_secret(secret_name):
    session = boto3.session.Session()
    client = session.client(
        service_name='secretsmanager',
    )

    try:
        get_secret_value_response = client.get_secret_value(SecretId=secret_name)
    except ClientError as e:
        raise e
    else:
        if 'SecretString' in get_secret_value_response:
            secret = get_secret_value_response['SecretString']
            return json.loads(secret)
        else:
            raise Exception("Secret binary not supported.")

def get_param(parameter_name):
    """
    Fetch a parameter value by name from AWS Systems Manager Parameter Store.
    
    Parameters:
    - parameter_name: str. The name of the parameter you want to retrieve.

    Returns:
    - The value of the requested parameter, or None if the parameter does not exist.
    """
    # Create a session using AWS credentials from the environment or config file
    session = boto3.session.Session()
    ssm = session.client('ssm')
    
    try:
        response = ssm.get_parameter(
            Name=parameter_name,
            WithDecryption=False  # Set to True if the parameter is encrypted
        )
        result = response['Parameter']['Value']
        return result
    except ClientError as e:
        print(f"An error occurred: {e}")
        return None

def any_empty(**kwargs):
    """
    Check if any given keyword argument is None or an empty string.

    Parameters:
    - **kwargs: Variable length keyword argument list.

    Returns:
    - True if all arguments are not None and not empty strings, otherwise returns a list of keys that are None or empty.
    """
    empty_keys = [key for key, value in kwargs.items() if value is None or value == ""]
    return len(empty_keys) > 0

# Fetching database connection details and new user details from environment variables
DB_HOST = get_env('DB_HOST')
DB_PORT = get_env('DB_PORT')
DB_NAME = get_env('DB_NAME')
DB_APP_USERNAME = get_env('DB_APP_USERNAME')
DB_APP_PASSWORD = get_env('DB_APP_PASSWORD')
SECRET_ARN = get_env('DB_SECRET_ARN')
 
# Connect to the PostgreSQL database
try:
    print("*** Creating application user ***")
    db_secret = get_secret(SECRET_ARN)
    username = db_secret['username']
    password = db_secret['password']
    
    app_username = get_param(DB_APP_USERNAME)
    app_password = get_param(DB_APP_PASSWORD)

    
    if any_empty(username=username, password=password, app_username=app_username, app_password=app_password):
        print("Credentials not found, exiting...")
        sys.exit(1)

        
    connection = psycopg2.connect(host=DB_HOST, port=DB_PORT, dbname=DB_NAME, user=username, password=password)
    connection.autocommit = True  # To ensure that commands are committed without having to call connection.commit()


    with connection.cursor() as cursor:
        print(f"** Verifying if the user {app_username} already exists")
        # Correctly passing a single parameter in a tuple
        cursor.execute("SELECT 1 FROM pg_roles WHERE rolname=%s", (app_username,))
        user_exists = cursor.fetchone() is not None
        print(f"** User {app_username} exists: {user_exists}")

        if user_exists:
            print(f"** Role '{app_username}' already exists. Dropping existing user.")
            # Using sql.SQL() with .format() for identifiers and keeping parameterized placeholders separate
            cursor.execute(sql.SQL("REVOKE ALL PRIVILEGES ON DATABASE {} FROM {}").format(
                sql.Identifier(DB_NAME), 
                sql.Identifier(app_username)
            ))
            cursor.execute(sql.SQL("DROP ROLE {}").format(
                sql.Identifier(app_username)
            ))
            print(f"** Role '{app_username}' dropped.")

        print(f"** Creating user '{app_username}' ")
        cursor.execute(sql.SQL("CREATE USER {} WITH PASSWORD %s").format(
            sql.Identifier(app_username)), 
            [app_password]
        )
        cursor.execute(sql.SQL("GRANT ALL PRIVILEGES ON DATABASE {} TO {}").format(
            sql.Identifier(DB_NAME), 
            sql.Identifier(app_username)
        ))
        cursor.execute(sql.SQL("GRANT ALL PRIVILEGES ON SCHEMA public TO {}").format(
            sql.Identifier(app_username)
        ))
        print(f"** User '{app_username}' created and granted all privileges on database '{DB_NAME}'.")

            
except Exception as e:
    print(f"An error occurred: {e}")
finally:
    if 'connection' in locals() and connection is not None:
        connection.close()
