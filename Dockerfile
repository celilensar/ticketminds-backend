# --- AŞAMA 1: BUILD (İnşaat) ---
# Maven yüklü bir imaj kullanıyoruz. Kodumuzu burada derleyeceğiz.
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Çalışma klasörünü ayarla
WORKDIR /app

# Önce sadece bağımlılık listesini (pom.xml) kopyala
# Bu, Docker'ın "Layer Caching" özelliğini kullanmasını sağlar.
# Yani kod değişse bile bağımlılıklar değişmediyse tekrar indirmez. Hız kazandırır!
COPY pom.xml .

# Bağımlılıkları indir (Kodları kopyalamadan önce)
RUN mvn dependency:go-offline

# Şimdi kaynak kodları kopyala
COPY src ./src

# Projeyi derle (Testleri atlayarak hız kazanıyoruz, çünkü testleri CI/CD'de koşturacağız)
RUN mvn clean package -DskipTests

# --- AŞAMA 2: RUN (Çalıştırma) ---
# Sadece Java'nın çalışması için gereken (JRE) hafif bir imaj kullanıyoruz.
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# İlk aşamadan (build) sadece üretilen .jar dosyasını alıyoruz
COPY --from=build /app/target/*.jar app.jar

# Uygulamanın çalışacağı portu belirtiyoruz
EXPOSE 8080

# Uygulamayı başlat
ENTRYPOINT ["java", "-jar", "app.jar"]