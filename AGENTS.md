# ksaypip — Agent Documentation

## Overview

This repository is a [Saypip](https://saypip.app) client library for Kotlin Multiplatform.
Saypip is a semi-anonymous SNS; the API is a contract-first, bearer-token API, and third-party
applications reach it through an OAuth 2.1 authorization server with PKCE.

Two things shape everything here and should be read before changing anything:

1. **There is no stable user ID.** People appear as *viewer-scoped* identity tokens (`vi_tok_…`)
   that are valid for one viewer alone and die with the relationship. Never introduce a model or
   a feature that assumes otherwise.
2. **A token cannot reach every route.** An allowlist in the API names what an application may
   call; the admin area, `GET /me/sign-in`, `DELETE /me`, the push endpoints and the realtime
   socket are cookie-only. Do not add resources for cookie-only routes to `core`.

The authoritative descriptions live in the Saypip repository (`docs/api-design.md`,
`packages/api-schema/src/routes.ts` and `schemas.ts`). When the deployment's contract changes,
this library follows it.

## Directory Structure

- **`core/`**: the API client.
  - `api/` — resource interfaces (`FeedResource`, `PostsResource`, …)
  - `api/request/` — one request class per endpoint (or per group), named `{Group}{Action}Request`
  - `api/response/` — response typealiases, named `{Group}{Action}Response`
  - `entity/` — the wire models (`@Serializable`, `@JsExport`)
  - `domain/` — closed sets (`MarkColor`, `MuteDuration`, `OAuthScope`, `ErrorCode`, …), modeled
    as string constants because the wire carries strings and JavaScript needs the values
  - `internal/` — implementations and HTTP plumbing
  - `util/` — `toBlocking`, header and media-type names
- **`auth/`**: OAuth 2.1 (authorization code + PKCE, refresh, revoke).
- **`all/`**: both modules in one artifact for CocoaPods / SPM / JS.
- **`plugins/`, `tool/`**: build configuration.

## Implementation Rules

- Every endpoint has a resource method, a `*Blocking` twin (`@JsExport.Ignore`), and, on the
  JavaScript side, a suspend function. Do not add a method without its blocking twin.
- Requests use mutable nullable fields and no constructor arguments, so a caller can build one
  field at a time and JavaScript can construct them.
- JSON bodies are built with `jsonBody { }` (`kotlinx.serialization.json`) rather than by
  serializing the request object, because an absent field and an explicit `null` mean different
  things on several writes and the request object also carries the transport-only
  `idempotencyKey`.
- `proceed { }` / `proceedUnit { }` / `proceedBytes { }` in `AbstractResourceImpl` are the only
  ways a response becomes `Response<T>`; errors become `SaypipException` with `code` and
  `reason` parsed from the envelope.
- Every path segment that a user supplies (post IDs, tags, emoji, identity tokens) goes through
  `InternalUtility.urlEncode`.
- Writes that the server makes idempotent take `idempotencyKey` and send it as
  `Idempotency-Key`; the key never goes into the body.
- `RawRequest` exists for exactly two requests — the media upload and a feedback screenshot —
  because their body/part content type is the caller's statement about the bytes, which
  khttpclient derives from a filename extension instead. Do not route anything else through it.

## Testing

```shell
./gradlew :core:jvmTest   # offline: serialization, request construction, error mapping
./gradlew :auth:jvmTest   # offline: PKCE (RFC 7636 vector) and the OAuth flow vs a local server
./gradlew jvmJar          # compile check without tests
```

Tests do not touch the network. Request construction is checked against a JDK `HttpServer`, which
is why the assertions are about the wire (`rawPath`, query, headers, body) rather than about the
deployment.

## Adding an Endpoint

1. Confirm the route in the Saypip API contract (`packages/api-schema/src/routes.ts`) and its
   schema (`schemas.ts`); note the success status (201 for creates) and whether the route is in
   the token allowlist (`apps/api/src/oauth-routes.ts`).
2. Add the request class under `api/request/{group}/`.
3. Add the response typealias under `api/response/{group}/`.
4. Add the method and its blocking twin to the resource interface, and implement both in
   `internal/{Group}ResourceImpl.kt`.
5. If the model in the response is new, add it under `entity/`.
6. Add an offline test to `RequestTest` (wire assertions) or `SerializationTest` (a fixture).

## Naming Conventions

| Type | Naming Pattern | Example |
| --- | --- | --- |
| Request | `{Group}{Action}Request` | `PostsCreateRequest` |
| Response | `{Group}{Action}Response` | `PostsCreateResponse` |
| Resource | `{Group}Resource` | `PostsResource` |
| Entity | Singular form | `Post`, `Person`, `Conversation` |
