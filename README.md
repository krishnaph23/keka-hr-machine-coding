# Keka machine-coding starter

Java 17 + Maven + Dropwizard 4.0.17. Includes Jersey REST endpoints, Jackson JSON,
SLF4J/Logback logging, Lombok, Java records, JUnit 5, and Java HttpClient.

## Run

Open this folder or `pom.xml` as a Maven project in IntelliJ with SDK 17.

```sh
cd /Users/krishnaphegde/Documents/Interviews/keka-machine-coding
mvn -o clean verify
mvn -o compile exec:java -Dexec.args="server config.yml"
```

Dependencies are cached; `-o` uses Maven offline mode. Omit `-o` when adding new
dependencies. Stop the server with Ctrl+C or the IDE Stop button.

For an IntelliJ Application run configuration:

- Main class: `org.example.Main`
- Program arguments: `server config.yml`
- Working directory: this project folder
- JDK: Java 17

Application port: **9090**. Dropwizard admin port: **9091**. Both bind to localhost.
The command passes two arguments to `Main.main`: `server` and `config.yml`.
`new Main().run(args)` tells Dropwizard to load that YAML file, read the inherited
server settings from `AppConfiguration`, and start its embedded Jetty server.
`environment.jersey().register(new EchoResource(service))` registers your API on
the application connector (9090). You do not pass the port to each resource.

To change the API port, edit `server.applicationConnectors[0].port` in `config.yml`
and restart the application. Keep the admin port different from the API port.
To validate configuration without starting the server:

```sh
mvn -o compile exec:java -Dexec.args="check config.yml"
```

## Try the POST endpoint

From another terminal:

```sh
curl -i http://localhost:9090/api/echo \
  -H 'Content-Type: application/json' \
  -d '{"message":"ready for Keka"}'
```

Expected: HTTP 200 and `{"message":"ready for Keka"}`.
Blank or missing messages and malformed JSON return HTTP 400. Jersey handles
unsupported methods (405), unsupported media types (415), and unknown paths (404).
`requests.http` contains IDE HTTP client examples.

## Where to code

```text
Main -> EchoResource -> EchoService -> response record -> JSON
```

- `Main.java`: Dropwizard Application; registers resources and creates services.
- `AppConfiguration.java`: extends Dropwizard Configuration. Add any custom settings here.
- `config.yml`: HTTP ports and console logging.
- `EchoResource.java`: JAX-RS `@Path`, `@POST`, `@Consumes`, `@Produces`; HTTP concerns.
- `EchoService.java`: business logic and request/response records.
- `ApiClient.java`: reusable Java HttpClient for outbound GET/POST JSON calls.
- `SetupTest.java`: starts the actual Dropwizard application on random ports and
  tests JSON round-tripping, validation, routing, and outbound error handling.
- `EchoServiceTest.java`: service and JSON mapping tests without a server.

Dropwizard 4 uses `jakarta.ws.rs` imports. If your earlier Dropwizard projects used
`javax.ws.rs`, use the `jakarta` equivalents in this project.

Lombok annotation processing is configured; `@RequiredArgsConstructor` wires the
resource constructor, and `@Slf4j` creates the application logger. Reload Maven in
the IDE; enable annotation processing and Lombok support if generated members
are shown as missing. Java records are ready for DTOs.

## Outbound calls

```java
ApiClient client = new ApiClient(); // create once and reuse
var response = client.post(
    URI.create("http://localhost:9090/api/echo"),
    new EchoService.EchoRequest("hello"),
    EchoService.EchoResponse.class);
// For an unknown JSON shape: client.get(uri, JsonNode.class)
```

Connection timeout: 5 seconds. Request timeout: 10 seconds. Non-2xx responses throw
IOException. No automatic retries or redirects. Methods expect a JSON body;
handle empty/204 responses separately if required. Add authentication headers
when the problem requires them. Propagate InterruptedException or restore the
interrupt flag when catching it.

## Extend during the round

Replace the echo resource with the given problem's endpoints. Keep HTTP concerns
in resources, business rules in services, and storage behind a domain-specific
repository interface with an in-memory implementation. No database is required.
Dropwizard handles requests concurrently, so protect compound storage operations
when necessary. A ConcurrentHashMap alone does not make multiple operations atomic.

## Before the interview — 27 November, 12:00 noon IST

1. Open the project in your normal IDE, run `mvn -o clean verify`, start the
   application, and try the cURL request before joining.
2. Review `Main`, `IncidentResource`, `IncidentService`, and `ApiClient` so you can explain them.
3. Disable AI coding assistants, AI autocomplete, and AI IDE plugins; close AI
   assistant windows before screen sharing. Check these settings manually.
4. Keep this README and permitted documentation/notes available.
5. Clarify requirements, build one complete flow, and test before adding optional
   features. Explain decisions and debugging aloud.

## After the interview

Build output, IDE files, logs, and `.env` are ignored by Git. Create a GitHub
repository, then run the following with your actual remote URL:

```sh
git init
git add .
git commit -m "Keka machine coding submission"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPOSITORY.git
git push -u origin main
```

Confirm the team can access the repository and send its link to the recruiter.
No repository has been published by this setup.

Framework reference: https://www.dropwizard.io/en/release-4.0.x/getting-started.html
