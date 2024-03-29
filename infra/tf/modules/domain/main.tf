# Create a hosted zone
resource "aws_route53_zone" "primary" {
  name = "${var.env_id}.${var.domain_name}"
}

# Request an ACM certificate
resource "aws_acm_certificate" "cert" {
  domain_name       = "${aws_route53_zone.primary.name}"
  validation_method = "DNS"
  
  options {
    certificate_transparency_logging_preference = "ENABLED"
  }

  tags = {
    Environment = var.env_id
  }
}

# Create validation records in the hosted zone
resource "aws_route53_record" "cert_validation" {
  for_each = {
    for dvo in aws_acm_certificate.cert.domain_validation_options : dvo.domain_name => {
      name   = dvo.resource_record_name
      record = dvo.resource_record_value
      type   = dvo.resource_record_type
    }
  }

  allow_overwrite = true
  name    = each.value.name
  records = [each.value.record]
  ttl     = 60
  type    = each.value.type
  zone_id = aws_route53_zone.primary.zone_id
}

resource "aws_acm_certificate_validation" "primary" {
  certificate_arn         = aws_acm_certificate.cert.arn
  validation_record_fqdns = [for record in aws_route53_record.cert_validation : record.fqdn]
}
