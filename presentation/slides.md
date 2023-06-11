---
marp: true
theme: uncover
---

# Open data server

https://github.com/fandreuz/open-data-server

---

## Vocabulary

### Collection

A set of datasets related to the same topic/experiment/event.

### Dataset

A table of heterogeneous data (CSV, JSON, ROOT).

---

## Logical data

---

## Physical data

---

## Unique identifiers

Uniform Resource Name (URN) standard: `schema:namespace:resourceName`

---

### Dataset ID

A dataset is identified by the schema, the namespace name (i.e. the collection it belongs to) and the file name.

### Example

We identify the file `experimentData` in the collection `19090` with the following URN:

```
cern-open-data:19090:experimentData
```

---

### Collection ID

A dataset is identified by the schema and its name. The last part of the URN is omitted since it's not needed.

### Example

We identify the collection `19090` with the following URN:

`cern-open-data:19090`

---

## Code structure

---

## REST endpoints

Full list in `README.md`

---

### `PUT /v1`

Idempotent creation of a new dataset in the database.

Example request body:

```json
{
  "collectionId": "211",
  "fileName": "qcd.root"
}
```

Sample interaction:

```
curl --header "Content-Type: application/json" \
  --request PUT \
  --data '{"collectionId":"13128","fileName":"237040910_DTHitsXZ.csv"}' \
  http://localhost:8080/v1
```

---

Sample output:

```json
{
  "id": "cern-open-data:13128:1686475345237",
  "fileName": "1686475345237.csv",
  "type": "CSV",
  "sizeInBytes": 735,
  "numberOfColumns": 3,
  "commaSeparatedColumnNames": "posX,posZ,driftDist",
  "importTimestamp": 1686475345479,
  "collectionMetadata": {
    "id": "cern-open-data:13128",
    "name": "13128",
    "shortDescription": "OPERA neutrino-induced charmed hadron event 237040910",
    "longDescription": "This OPERA detector event is a muon neutrino interaction 
      with the lead target where a charmed hadron was reconstructed in the final state.
      The event data consist of Electronic Detector files (such as Drift Tube, RPC, and
      Target Tracker files) and Emulsion Detector files (such as Tracks and Vertex files).
      For more information, see the description of the whole dataset.",
    "year": 2019,
    "experimentName": "OPERA",
    "eventsCount": 1,
    "type": "Derived",
    "keyword": "",
    "tag": "CERN-SPS",
    "citeText": "Cite as: OPERA collaboration (2019). OPERA neutrino-induced charmed hadron 
      event 237040910. CERN Open Data Portal. ",
    "doi": "10.7483/OPENDATA.OPERA.Q74R.SYBQ",
    "license": "Creative Commons CC0 waiver"
  }
}
```

---

### `GET /v1/{id}/{column-name}`

Get the content of a column for the selected dataset.

Sample interaction:

```
curl -i --request GET \  
    http://localhost:8080/v1/cern-open-data:13128:1686475345237/posX
```

Sample output:

```json
{
  "647fa76f10c98516828586ae": "66.55",
  "647fa76f10c98516828586af": "64.45",
   ...
}
```

---

## Native executable

```bash
[fandreuz@pc-342 root-data-server]$ sudo ./gradlew build \
    -Dquarkus.package.type=native \
    -Dquarkus.native.container-build=true \
    -Dquarkus.native.container-runtime=podman
[1/7] Initializing...                                                                                   (13.3s @ 0.28GB)
 Version info: 'GraalVM 22.3.2.1-Final Java 17 Mandrel Distribution'
 Java version info: '17.0.7+7'
 C compiler: gcc (redhat, x86_64, 8.5.0)
 Garbage collector: Serial GC
 3 user-specific feature(s)
 - io.quarkus.hibernate.validator.runtime.DisableLoggingFeature: Disables INFO logging during the analysis phase for the [org.hibernate.validator.internal.util.Version] categories
 - io.quarkus.runner.Feature: Auto-generated class by Quarkus from the existing extensions
 - io.quarkus.runtime.graal.DisableLoggingFeature: Disables INFO logging during the analysis phase
 [2/7] Performing analysis...  [*******]                                                                 (85.2s @ 2.70GB)
  14,872 (89.39%) of 16,638 classes reachable
  20,771 (60.57%) of 34,295 fields reachable
  73,401 (57.50%) of 127,659 methods reachable
     583 classes,   187 fields, and 3,055 methods registered for reflection
      63 classes,    68 fields, and    55 methods registered for JNI access
       4 native libraries: dl, pthread, rt, z
[3/7] Building universe...                                                                              (12.1s @ 2.13GB)
[4/7] Parsing methods...      [***]                                                                      (9.9s @ 3.26GB)
[5/7] Inlining methods...     [***]                                                                      (6.9s @ 2.05GB)
[6/7] Compiling methods...    [*********]                                                               (83.6s @ 1.19GB)
[7/7] Creating image...                                                                                  (6.8s @ 3.40GB)
  26.74MB (49.02%) for code area:    47,104 compilation units
  27.48MB (50.37%) for image heap:  353,319 objects and 16 resources
 339.15KB ( 0.61%) for other data
  54.55MB in total
------------------------------------------------------------------------------------------------------------------------
Top 10 packages in code area:                               Top 10 object types in image heap:
   1.63MB sun.security.ssl                                     6.01MB byte[] for code metadata
   1.02MB java.util                                            3.52MB java.lang.Class
 745.45KB java.lang.invoke                                     3.30MB java.lang.String
 717.67KB com.sun.crypto.provider                              2.83MB byte[] for general heap data
 526.25KB java.lang                                            2.71MB byte[] for java.lang.String
 493.69KB com.mongodb.internal.connection                      1.25MB com.oracle.svm.core.hub.DynamicHubCompanion
 450.58KB sun.security.x509                                  776.12KB byte[] for reflection metadata
 430.22KB io.quarkus.runtime.generated                       764.77KB java.util.HashMap$Node
 421.85KB java.util.concurrent                               691.95KB java.lang.String[]
 404.22KB io.netty.buffer                                    526.02KB c.o.svm.core.hub.DynamicHub$ReflectionMetadata
  19.65MB for 632 more packages                                4.87MB for 3782 more object types
                        9.1s (4.0% of total time) in 39 GCs | Peak RSS: 5.67GB | CPU load: 6.50
Produced artifacts:
 /project/root-data-server-1.0.0-SNAPSHOT-runner (executable)
 /project/root-data-server-1.0.0-SNAPSHOT-runner-build-output-stats.json (json)
 /project/root-data-server-1.0.0-SNAPSHOT-runner-timing-stats.json (raw)
 /project/root-data-server-1.0.0-SNAPSHOT-runner.build_artifacts.txt (txt)
```

---

## Server Dockerfile

```Docker
# Build native image
FROM quay.io/quarkus/ubi-quarkus-mandrel-builder-image:22.3-java17 AS build
USER root
RUN microdnf install findutils
COPY --chown=quarkus:quarkus gradlew /code/gradlew
COPY --chown=quarkus:quarkus gradle /code/gradle
COPY --chown=quarkus:quarkus build.gradle.kts /code/
COPY --chown=quarkus:quarkus settings.gradle.kts /code/
COPY --chown=quarkus:quarkus gradle.properties /code/
USER quarkus
WORKDIR /code
COPY src /code/src
RUN ./gradlew build -Dquarkus.package.type=native

# Go executables to deal with .root files
FROM golang:1.20 AS go-root
RUN go install go-hep.org/x/hep/cmd/root2csv@latest && cp $GOPATH/bin/root2csv /usr/local/bin/
RUN go install go-hep.org/x/hep/groot/cmd/root-ls@latest && cp $GOPATH/bin/root-ls /usr/local/bin/

# Create docker image
FROM quay.io/quarkus/quarkus-micro-image:2.0
WORKDIR /work
COPY --from=build /code/build/*-runner /work/runner
COPY --from=go-root /usr/local/bin/root2csv /work/go/bin/
COPY --from=go-root /usr/local/bin/root-ls /work/go/bin/
RUN chmod 775 /work
EXPOSE 8080
CMD ["./runner", "-Dquarkus.http.host=0.0.0.0", "-Droot.executables.path=/work/go/bin"]
```

---

## Full application

The full application is bundled as a `docker-compose.yml` file:
```yml
services:
  server:
    build: .
    ports:
      - "8080:8080"
    environment:
      - mongodb.uri=mongodb://mongo:27017
  mongo:
    image: "mongo:latest"
    ports:
      - "27017"
```

---

## Thanks for your attention