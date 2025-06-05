# Estágio 1: Compilar a aplicação usando Gradle
FROM gradle:8.8.0-jdk21 AS build

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia os arquivos de build do Gradle E o script do wrapper
COPY build.gradle settings.gradle gradlew /app/
COPY gradle /app/gradle

# Copia o código fonte
COPY src /app/src

# Concede permissões de execução ao gradlew
RUN chmod +x ./gradlew

# Compila o arquivo JAR da aplicação
# Use --no-daemon para evitar problemas em alguns ambientes de CI/CD
RUN ./gradlew build --no-daemon -x test

# Estágio 2: Criar a imagem final leve
FROM eclipse-temurin:21-jre-jammy

# Cria um usuário e grupo não-root
RUN groupadd --gid 1001 appgroup && \
    useradd --uid 1001 --gid appgroup --create-home --shell /bin/bash appuser

# Define o diretório de trabalho
WORKDIR /app

# Copia o arquivo JAR do estágio de compilação
COPY --from=build /app/build/libs/*.jar app.jar

# Define a variável de ambiente para a URL do banco de dados (exemplo, pode ser sobrescrita)
ENV DB_URL=jdbc:postgresql://postgres-db:5432/extreme_events_db

# Expõe a porta da aplicação (padrão 8080, pode ser sobrescrita pela variável de ambiente SERVER_PORT)
EXPOSE 8080

# Altera a propriedade do diretório da aplicação e do arquivo JAR para o usuário não-root
# Garante que o usuário exista antes de alterar a propriedade
RUN chown -R appuser:appgroup /app

# Alterna para o usuário não-root
USER appuser

# Define o ponto de entrada para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]