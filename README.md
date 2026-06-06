# SafeFaces – README

Detta dokument innehåller instruktioner för hur man kör projektet **SafeFaces**.

---


Koden finns i följande GitHub-repository:

https://github.com/gyuni1337/SafeFaces

Den version som ska granskas är:
- Branch: `master`

---
**Förutsättningar:**

- Java JDK 11 eller senare
- Maven
- PostgreSQL

---
**Databas:**

Projektet använder PostgreSQL som databas. Anslutningsinställningar hanteras via filen:
src/main/resources/dbdata/config.properties

Se till att:
- PostgreSQL är igång
- databasen är tillgänglig (t.ex. via localhost om tunnel används)
- användarnamn och lösenord i config.properties är korrekta

---
**Externa bibliotek:**

Projektet använder Maven för att hantera externa bibliotek och beroenden. Dessa laddas ner automatiskt när projektet öppnas eller byggs i en IDE med Maven-stöd.

---

**För att starta applikationen:**

1. Klona projektet:
   ```bash
   git clone https://github.com/gyuni1337/SafeFaces.git
2. Öppna projektet i valfri IDE (t.ex. IntelliJ).

3. Starta applikationen via klassen (src/main/java/com.safefaces.safefaces/Javafx/App):
   - Launcher
