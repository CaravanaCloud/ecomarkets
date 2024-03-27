output api_endpoint {
  value = "https://${aws_api_gateway_rest_api.that.id}.execute-api.${var.aws_region}.amazonaws.com/${aws_api_gateway_stage.that.stage_name}"
}