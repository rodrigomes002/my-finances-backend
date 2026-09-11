# Stage 1: Build
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copiar arquivos de configuração Maven
COPY pom.xml .

# Download de dependências (cacheable layer)
RUN mvn dependency:go-offline

# Copiar código-fonte
COPY src ./src

# Build da aplicação
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiar JAR da aplicação do estágio de build
COPY --from=builder /app/target/my-finances-backend-1.0.0.jar app.jar

# Expor porta
EXPOSE 8080

# Comando de inicialização
ENTRYPOINT ["java", "-jar", "app.jar"]
