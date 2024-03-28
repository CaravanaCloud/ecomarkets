output api_endpoint {
    value = module.api.api_endpoint
}

output web_endpoint {
    value = "${module.ecs.web_endpoint}"
}

output alb_dns_name {
    value = "${module.ecs.alb_dns_name}"
}

output web_endpoint {
    value = "https://${var.env_id}.${var.domain_name}"
}