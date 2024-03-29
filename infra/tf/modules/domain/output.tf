output "certificate_arn" {
  value = aws_acm_certificate.cert.arn
}

output "zone_id" {
  value = aws_route53_zone.primary.zone_id
}

output "name_servers" {
  value = aws_route53_zone.primary.name_servers
}

output "name_servers_str" {
  value = join("\n", aws_route53_zone.primary.name_servers)
}
