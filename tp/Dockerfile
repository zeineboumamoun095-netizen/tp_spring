# ─────────────────────────────────────────────────────────────────
#  STAGE 1 : Build avec Maven
# ─────────────────────────────────────────────────────────────────
FROM maven:3.9.5-eclipse-temurin-17 AS build

WORKDIR /app

# Copier pom.xml en premier pour bénéficier du cache Docker
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copier le code source et compiler
COPY src ./src
RUN mvn clean package -DskipTests -B

# ─────────────────────────────────────────────────────────────────
#  STAGE 2 : Image légère pour l'exécution
# ─────────────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Créer un utilisateur non-root (sécurité)
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copier le JAR depuis le stage de build
COPY --from=build /app/target/*.jar app.jar

# Variables d'environnement par défaut
ENV SPRING_PROFILES_ACTIVE=prod
ENV SERVER_P
ORT=8080

EXPOSE 8080

# Point d'entrée
ENTRYPOINT ["java", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", \
  "-jar", "app.jar"]