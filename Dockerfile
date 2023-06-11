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