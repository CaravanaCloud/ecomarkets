output db_endpoint {
  value = aws_rds_cluster.aurora_cluster.endpoint
}

output db_name {
  value = aws_rds_cluster.aurora_cluster.database_name           
}