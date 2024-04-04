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
    if empty_keys:
        return True
    else:
        print(f"Empty or None values for keys: {', '.join(empty_keys)}")
        return False

# Fetching database connection details and new user details from environment variables
DB_HOST = get_env('DB_HOST')
DB_PORT = get_env('DB_PORT')
DB_NAME = get_env('DB_NAME')
DB_APP_USERNAME = get_env('DB_APP_USERNAME')
DB_APP_PASSWORD = get_env('DB_APP_PASSWORD')
SECRET_ARN = get_env('DB_SECRET_ARN')
 
# Connect to the PostgreSQL database
try:
    print("*** create application user ***")
    db_secret = get_secret(SECRET_ARN)
    username = db_secret['username']
    password = db_secret['password']
    
    app_username = get_param(DB_APP_USERNAME)
    app_password = get_param(DB_APP_PASSWORD)

    
    if any_empty(username=username, password=password, app_username=app_username, app_password=app_password):
        print("Credentials not found, exiting...")
        # print(" username - " + username)
        # print(" password - " + password)
        # print(" app_username - " + app_username)
        # print(" app_password - " + app_password)
        sys.exit(1)

        
    connection = psycopg2.connect(host=DB_HOST, port=DB_PORT, dbname=DB_NAME, user=username, password=password)
    connection.autocommit = True  # To ensure that commands are committed without having to call connection.commit()

    with connection.cursor() as cursor:
        # Check if the user (role) already exists
        cursor.execute("SELECT 1 FROM pg_roles WHERE rolname=%s", (DB_APP_USERNAME,))
        user_exists = cursor.fetchone() is not None

        if not user_exists:
            # Create a new user (role) with the provided credentials if it does not exist
            cursor.execute(sql.SQL("CREATE USER {} WITH PASSWORD %s").format(sql.Identifier(app_username)), [app_password])

            # Grant all privileges on the database to the new user
            cursor.execute(sql.SQL("GRANT ALL PRIVILEGES ON DATABASE {} TO {}").format(sql.Identifier(DB_NAME), sql.Identifier(app_username)))

            print(f"** User '{app_username}' created and granted all privileges on database '{DB_NAME}'.")
        else:
            print(f"** User '{app_username}' already exists. No action taken.")
except Exception as e:
    print(f"An error occurred: {e}")
finally:
    if 'connection' in locals() and connection is not None:
        connection.close()
