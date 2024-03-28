import pymysql

# Database connection parameters
host = "your_aurora_endpoint"
user = "dbadmin"
password = "yourpassword"
db = "mydatabase"

# Connect to the database
connection = pymysql.connect(host=host,
                             user=user,
                             password=password,
                             database=db)

try:
    with connection.cursor() as cursor:
        # Execute a query
        sql = "CREATE TABLE example_table (id INT AUTO_INCREMENT PRIMARY KEY, data VARCHAR(255))"
        cursor.execute(sql)

    # Commit changes
    connection.commit()
finally:
    connection.close()