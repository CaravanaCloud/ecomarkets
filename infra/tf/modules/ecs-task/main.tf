data "aws_ssm_parameter" "db_app_username" {
  name = var.db_app_username
}

data "aws_ssm_parameter" "db_app_password" {
  name = var.db_app_password
}

data "aws_ssm_parameter" "twilio_account_sid" {
  name = var.twilio_account_sid
}

data "aws_ssm_parameter" "twilio_auth_token" {
  name = var.twilio_auth_token
}

data "aws_ssm_parameter" "twilio_phone_from" {
  name = var.twilio_phone_from
}

data "aws_ssm_parameter" "oidc_client_id" {
  name = var.oidc_client_id
}

data "aws_ssm_parameter" "oidc_client_secret" {
  name = var.oidc_client_secret
}

data "aws_ssm_parameter" "oidc_provider" {
  name = var.oidc_provider
}


resource "aws_cloudwatch_log_group" "task_logs" {
  name              = "/${var.env_id}/${var.task_id}"
  retention_in_days = 7

  # Optionally, add tags to help organize and manage your log group
  tags = {
    EnvId = var.env_id
    TaskId = var.task_id
  }
}


resource "aws_security_group" "task_sg" {
  name_prefix = "${var.task_id}_sg"
  description = "Allow inbound traffic from VPC"
  vpc_id      = var.vpc_id

  ingress {
    from_port   = var.container_port
    to_port     = var.container_port
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"] # TODO restrict to LB
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
  tags = {
    Name = "${var.task_id}_sg"
  }
}



resource "aws_lb_target_group" "task_target_group" {
  name_prefix = var.task_id
  port        = var.container_port
  protocol    = "HTTP"
  vpc_id      = var.vpc_id
  target_type = "ip"

  health_check {
    enabled             = true
    healthy_threshold   = 3
    unhealthy_threshold = 3
    timeout             = 5
    path                = "/${var.task_id}/"
    protocol            = "HTTP"
    matcher             = "200"
    interval            = 15
  }

  tags = {
    Name = "${var.task_id}-target"
    Env  = var.env_id
  }
}


resource "aws_lb_listener_rule" "task_rule" {
  depends_on   = [aws_lb_target_group.task_target_group]
  listener_arn = var.listener_arn
  priority = var.priority

  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.task_target_group.arn
  }

  condition {
    path_pattern {
      values = [var.path_pattern]
    }
  }
}



resource "aws_ecs_task_definition" "task_def" {
  family                   = "${var.env_id}_${var.task_id}"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  execution_role_arn       = var.task_execution_role
  cpu                      = var.container_cpu
  memory                   = var.container_mem

  container_definitions = jsonencode([
    {
      name      = "${var.task_id}_container",
      image     = var.container_image,
      cpu       = var.container_cpu,
      memory    = var.container_mem,
      essential = true,

      environment = [
        {
          name  = "QUARKUS_PROFILE",
          value = "prod"
          }, {
          name  = "QUARKUS_DATASOURCE_JDBC_URL",
          value = "jdbc:postgresql://${var.db_endpoint}/${var.db_name}"
          }, {
          name  = "QUARKUS_DATASOURCE_USERNAME",
          value = data.aws_ssm_parameter.db_app_username.value
          }, {
          name  = "QUARKUS_DATASOURCE_PASSWORD",
          value = data.aws_ssm_parameter.db_app_password.value
          }, {
          name  = "QUARKUS_OIDC_PROVIDER",
          value = data.aws_ssm_parameter.oidc_provider.value
          }, {
          name  = "QUARKUS_OIDC_CLIENT_ID",
          value = data.aws_ssm_parameter.oidc_client_id.value
          }, {
          name  = "QUARKUS_OIDC_CREDENTIALS_SECRET",
          value = data.aws_ssm_parameter.oidc_client_secret.value
          }, {
          name  = "TWILIO_ACCOUNT_SID",
          value = data.aws_ssm_parameter.twilio_account_sid.value
          }, {
          name  = "TWILIO_AUTH_TOKEN",
          value = data.aws_ssm_parameter.twilio_auth_token.value
          }, {
          name  = "TWILIO_PHONE_FROM",
          value = data.aws_ssm_parameter.twilio_phone_from.value
          }
      ]

      portMappings = [
        {
          containerPort = var.container_port,
          hostPort      = var.container_port,
          protocol      = "tcp"
        },
      ]

      logConfiguration = {
        logDriver = "awslogs"
        options = {
          awslogs-group         = "/${var.env_id}/${var.task_id}"
          awslogs-region        = "${var.aws_region}"
          awslogs-stream-prefix = "ecslogs"
        }
      }
    },
  ])
}

resource "aws_ecs_service" "task_service" {
  depends_on      = [aws_lb_listener_rule.task_rule]
  name            = "${var.env_id}_${var.task_id}"
  cluster         = var.cluster_id
  task_definition = aws_ecs_task_definition.task_def.arn
  launch_type     = "FARGATE"
  

  network_configuration {
    subnets          = var.ecs_subnets
    security_groups  = [aws_security_group.task_sg.id]
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.task_target_group.arn
    container_name   = "${var.task_id}_container"
    container_port   = var.container_port
  }

  desired_count = 1
}
