output cluster_id {
    value = aws_ecs_cluster.that.id
}

output task_execution_role {
    value = aws_iam_role.ecs_task_execution_role.arn
}

output alb_dns_name {
    value = aws_lb.external.dns_name
}