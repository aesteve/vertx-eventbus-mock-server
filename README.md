### Vert.x Eventbus mock server

Just a fake Vert.x application listening and replying to messages sent over the eventbus, and bridging the eventbus on port 7542 TCP.
Useful if you need to test an Eventbus client implementation for instance, without mocking the TCP connection.

Spec:
* bridges the event-bus on `TCP:7542` 
* echoes back every message sent to `echo-address` (you should use `the-reply-address` as reply address, it's the only one allowed in outbound permissions)
* publishes a random UUID every 1 second on `out-address`
* fails (sends a failed response) to every message sent to `error-address`

Available as a [docker image](https://hub.docker.com/layers/aesteve/tests/mock-eventbus-server/images/sha256-6bf84bd762073eba6ef9391be3cba87c2f57c148efc7eb098fd2fc0d12c213e1?context=repo).
```
docker run -p 7542:7542 aesteve/tests:mock-eventbus-server
```

