resource "null_resource" "db_query" {
  provisioner "local-exec" {
    command = "pip3 install psycopg2 && python3 query.py"
  
    environment = {
      DB_HOST = var.db_host
      DB_PORT = var.db_port
      DB_NAME = var.db_name
      DB_USERNAME = var.db_username
      DB_PASSWORD = var.db_password
      DB_APP_USERNAME = var.db_app_username
      DB_APP_PASSWORD = var.db_app_password
    }  
  }
}