resource "terraform_data" "db_query" {
  provisioner "local-exec" {
    command = "pip3 install --force-reinstall psycopg2 boto3 && python3 query.py"
  
    environment = {
      DB_HOST = var.db_endpoint
      DB_PORT = var.db_port
      DB_NAME = var.db_name
      DB_APP_USERNAME = var.db_app_username
      DB_APP_PASSWORD = var.db_app_password
      DB_SECRET_ARN = var.db_master_user_secret
    }  
  }
}