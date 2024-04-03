output db_master_user_secret {
  value = aws_rds_cluster.aurora_cluster.master_user_secret
}

output db_endpoint {
  value = aws_rds_cluster.aurora_cluster.endpoint
}

output db_host {
  value = aws_rds_cluster.aurora_cluster.endpoint
}

output db_name {
  value = aws_rds_cluster.aurora_cluster.database_name           
}

output db_port {
  value = aws_rds_cluster.aurora_cluster.port
}