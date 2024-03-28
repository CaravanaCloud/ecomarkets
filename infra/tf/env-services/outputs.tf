output alb_dns_name {
    value = "${module.ecs.alb_dns_name}"
}

output ecs_endpoint {
    value = "https://${var.env_id}.${var.domain_name}"
}
