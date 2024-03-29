import os
import subprocess
import sys

# Function to check and install psycopg2 if necessary
def install_package(package_name):
    try:
        __import__(package_name)
    except ImportError:
        print(f"{package_name} is not installed. Installing...")
        subprocess.check_call([sys.executable, "-m", "pip", "install", package_name + "-binary"])  # Using psycopg2-binary for easier installation
        print(f"{package_name} installed successfully.")

# Check and install psycopg2-binary
install_package("psycopg2")

import psycopg2
from psycopg2 import sql

# Function to get environment variables
def get_env(var_name):
    try:
        return os.environ[var_name]
    except KeyError:
        raise Exception(f"Environment variable {var_name} not found.")

# Fetching database connection details and new user details from environment variables
DB_HOST = get_env('DB_HOST')
DB_PORT = get_env('DB_PORT')
DB_NAME = get_env('DB_NAME')
DB_USERNAME = get_env('DB_USERNAME')
DB_PASSWORD = get_env('DB_PASSWORD')
DB_APP_USERNAME = get_env('DB_APP_USERNAME')
DB_APP_PASSWORD = get_env('DB_APP_PASSWORD')

# Connect to the PostgreSQL database
try:
    connection = psycopg2.connect(host=DB_HOST, port=DB_PORT, dbname=DB_NAME, user=DB_USERNAME, password=DB_PASSWORD)
    connection.autocommit = True  # To ensure that commands are committed without having to call connection.commit()

    with connection.cursor() as cursor:
        # Check if the user (role) already exists
        cursor.execute("SELECT 1 FROM pg_roles WHERE rolname=%s", (DB_APP_USERNAME,))
        user_exists = cursor.fetchone() is not None

        if not user_exists:
            # Create a new user (role) with the provided credentials if it does not exist
            cursor.execute(sql.SQL("CREATE USER {} WITH PASSWORD %s").format(sql.Identifier(DB_APP_USERNAME)), [DB_APP_PASSWORD])

            # Grant all privileges on the database to the new user
            cursor.execute(sql.SQL("GRANT ALL PRIVILEGES ON DATABASE {} TO {}").format(sql.Identifier(DB_NAME), sql.Identifier(DB_APP_USERNAME)))

            print(f"User '{DB_APP_USERNAME}' created and granted all privileges on database '{DB_NAME}'.")
        else:
            print(f"User '{DB_APP_USERNAME}' already exists. No action taken.")
except Exception as e:
    print(f"An error occurred: {e}")
finally:
    if 'connection' in locals() and connection is not None:
        connection.close()
