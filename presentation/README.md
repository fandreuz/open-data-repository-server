# Presentation

## Ideas

### Why Quarkus

- Native executable
- Faster start-up wrt Spring Boot + JVM

### Choose JSON over XML

Why?
  - Quarkus integration
  - Easier (more pleasant to write `curl` commands by hand)
  - More standard
  - Faster(?)

What do we loose?
  - No schema validation
    - Not a problem because we do that in the server
  - Less types
    - We have strings only