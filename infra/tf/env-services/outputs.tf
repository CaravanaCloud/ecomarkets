output api_endpoint {
    value = module.api.api_endpoint
}

output web_endpoint {
    value = "https://${module.ecs.web_endpoint}"
}