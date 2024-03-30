data "aws_ssm_parameter" "db_username" {
  name = var.db_username
}

data "aws_ssm_parameter" "db_password" {
  name = var.db_password
}

# ECS Cluster
resource "aws_ecs_cluster" "that" {
  name = "${var.env_id}-ecs"
}

# IAM Role for ECS Task Execution
resource "aws_iam_role" "ecs_task_execution_role" {
  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Action = "sts:AssumeRole",
        Effect = "Allow",
        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        },
      },
    ],
  })
}

resource "aws_iam_role_policy_attachment" "ecs_task_execution_role_policy" {
  role       = aws_iam_role.ecs_task_execution_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

resource "aws_iam_policy" "ecs_secrets_policy" {
  description = "Allow ECS Task Execution Role to retrieve secrets"

  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Action = [
          "secretsmanager:GetSecretValue",
        ],
        Resource = [
          "arn:aws:secretsmanager:us-west-2:123456789012:secret:DockerRegistryCredentials", # The ARN of your secret
        ],
        Effect = "Allow",
      },
    ],
  })
}

resource "aws_iam_role_policy_attachment" "ecs_secrets_policy_attach" {
  role       = aws_iam_role.ecs_task_execution_role.name
  policy_arn = aws_iam_policy.ecs_secrets_policy.arn
}

resource "aws_cloudwatch_log_group" "ecs_api_logs" {
  name              = "/${var.env_id}/api"
  retention_in_days = 7

  # Optionally, add tags to help organize and manage your log group
  tags = {
    EnvId = var.env_id
  }
}

resource "aws_cloudwatch_log_group" "ecs_web_logs" {
  name              = "/${var.env_id}/web"
  retention_in_days = 7

  # Optionally, add tags to help organize and manage your log group
  tags = {
    EnvId = var.env_id
  }
}

# Task Definition

resource "aws_security_group" "ecs_worker_web_sg" {
  name_prefix = "ecs_worker_web_sg"
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
    Name = "worker_web_sg"
  }
}

resource "aws_security_group" "ecs_worker_api_sg" {
  name_prefix = "ecs_worker_api_sg"
  description = "Allow inbound traffic from VPC"
  vpc_id      = var.vpc_id

  ingress {
    from_port   = var.container_api_port
    to_port     = var.container_api_port
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
    Name = "worker_api_sg"
  }
}

resource "aws_security_group" "ecs_lb_sg" {
  name_prefix = "ecs_lb_sg"
  description = "Allow inbound traffic from VPC"
  vpc_id      = var.vpc_id

  ingress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"] # TODO Restrict to CDN
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "lb-sg"
  }
}

resource "aws_lb" "ecs_alb" {
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.ecs_lb_sg.id]
  subnets            = var.ecs_subnets

  enable_deletion_protection = false

  tags = {
    Name = "ecsALB"
    Env  = var.env_id
  }

  timeouts {
    create = "15m"
    update = "15m"
  }
}

resource "aws_lb_target_group" "web_target" {
  port        = var.container_port
  protocol    = "HTTP"
  vpc_id      = var.vpc_id
  target_type = "ip"

  health_check {
    enabled             = true
    healthy_threshold   = 3
    unhealthy_threshold = 3
    timeout             = 5
    path                = "/"
    protocol            = "HTTP"
    matcher             = "200"
    interval            = 15
  }

  tags = {
    Name = "web-target"
    Env  = var.env_id
  }
}

resource "aws_lb_target_group" "api_target" {
  port        = var.container_api_port
  protocol    = "HTTP"
  vpc_id      = var.vpc_id
  target_type = "ip"

  health_check {
    enabled             = true
    healthy_threshold   = 3
    unhealthy_threshold = 3
    timeout             = 5
    path                = "/"
    protocol            = "HTTP"
    matcher             = "200"
    interval            = 15
  }

  tags = {
    Name = "api-target"
    Env  = var.env_id
  }
}



resource "aws_lb_listener_rule" "api_rule" {
  depends_on   = [aws_lb_target_group.api_target]
  listener_arn = aws_lb_listener.ecs_listener.arn
  priority     = 100

  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.api_target.arn
  }

  condition {
    path_pattern {
      values = ["/api/*"]
    }
  }
}

resource "aws_lb_listener_rule" "web_rule" {
  depends_on   = [aws_lb_target_group.web_target]
  listener_arn = aws_lb_listener.ecs_listener.arn
  priority     = 101

  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.web_target.arn
  }

  condition {
    path_pattern {
      values = ["/*"]
    }
  }
}


resource "aws_lb_listener" "ecs_listener" {
  depends_on        = [aws_lb_target_group.web_target, aws_lb_target_group.api_target]
  load_balancer_arn = aws_lb.ecs_alb.arn
  port              = 443
  protocol          = "HTTPS"
  certificate_arn   = var.certificate_arn

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.web_target.arn
  }
}


resource "aws_ecs_task_definition" "web_task" {
  family                   = "web-tasks"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  execution_role_arn       = aws_iam_role.ecs_task_execution_role.arn
  cpu                      = var.container_cpu
  memory                   = var.container_mem

  container_definitions = jsonencode([
    {
      name      = "web_container",
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
          value = data.aws_ssm_parameter.db_username.value
          }, {
          name  = "QUARKUS_DATASOURCE_PASSWORD",
          value = data.aws_ssm_parameter.db_password.value
          }, {
          name  = "QUARKUS_OIDC_PROVIDER",
          value = var.oidc_provider
          }, {
          name  = "QUARKUS_OIDC_CLIENT_ID",
          value = var.oidc_client_id
          }, {
          name  = "QUARKUS_OIDC_CREDENTIALS_SECRET",
          value = var.oidc_client_secret
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
          awslogs-group         = "/${var.env_id}/web"
          awslogs-region        = "${var.aws_region}"
          awslogs-stream-prefix = "ecslogs"
        }
      }
    },
  ])
}


resource "aws_ecs_task_definition" "api_task" {
  family                   = "api-tasks"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  execution_role_arn       = aws_iam_role.ecs_task_execution_role.arn
  cpu                      = var.container_api_cpu
  memory                   = var.container_api_mem

  container_definitions = jsonencode([
    {
      name      = "api_container",
      image     = var.container_api_image,
      cpu       = var.container_api_cpu,
      memory    = var.container_api_mem,
      essential = true,

      portMappings = [
        {
          containerPort = var.container_api_port,
          hostPort      = var.container_api_port,
          protocol      = "tcp"
        },
      ]

      environment = [
        {
          name  = "QUARKUS_PROFILE",
          value = "prod"
          }, {
          name  = "QUARKUS_DATASOURCE_JDBC_URL",
          value = "jdbc:postgresql://${var.db_endpoint}/${var.db_name}"
          }, {
          name  = "QUARKUS_DATASOURCE_USERNAME",
          value = data.aws_ssm_parameter.db_username.value
          }, {
          name  = "QUARKUS_DATASOURCE_PASSWORD",
          value = data.aws_ssm_parameter.db_password.value
          }, {
          name  = "QUARKUS_OIDC_PROVIDER",
          value = var.oidc_provider
          }, {
          name  = "QUARKUS_OIDC_CLIENT_ID",
          value = var.oidc_client_id
          }, {
          name  = "QUARKUS_OIDC_CREDENTIALS_SECRET",
          value = var.oidc_client_secret
        }
      ]

      logConfiguration = {
        logDriver = "awslogs"
        options = {
          awslogs-group         = "/${var.env_id}/api"
          awslogs-region        = "${var.aws_region}"
          awslogs-stream-prefix = "ecs"
        }
      }
    },
  ])
}

# ECS Service
resource "aws_ecs_service" "web_service" {
  depends_on      = [aws_lb_listener_rule.web_rule]
  name            = "${var.env_id}_service_web"
  cluster         = aws_ecs_cluster.that.id
  task_definition = aws_ecs_task_definition.web_task.arn
  launch_type     = "FARGATE"

  network_configuration {
    subnets          = var.ecs_subnets
    security_groups  = [aws_security_group.ecs_worker_web_sg.id]
    assign_public_ip = true
  }

  load_balancer {

    target_group_arn = aws_lb_target_group.web_target.arn
    container_name   = "web_container"
    container_port   = var.container_port
  }

  desired_count = 1
}

resource "aws_ecs_service" "api_service" {
  depends_on      = [aws_lb_listener_rule.api_rule]
  name            = "${var.env_id}_service_api"
  cluster         = aws_ecs_cluster.that.id
  task_definition = aws_ecs_task_definition.api_task.arn
  launch_type     = "FARGATE"

  network_configuration {
    subnets          = var.ecs_subnets
    security_groups  = [aws_security_group.ecs_worker_api_sg.id]
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.api_target.arn
    container_name   = "api_container"
    container_port   = var.container_api_port
  }

  desired_count = 1
}


resource "aws_route53_record" "ecs_lb_dns" {
  zone_id = var.zone_id
  name    = var.env_id
  type    = "A"

  alias {
    name                   = aws_lb.ecs_alb.dns_name
    zone_id                = aws_lb.ecs_alb.zone_id
    evaluate_target_health = true
  }
}
