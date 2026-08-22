# BikeDisplayCompanion

Foundation implementation for an EKD01-UART e-bike display companion app.

## Current implementation

This repository now includes a working multi-module baseline with:

- Android app module (`app`) that can produce release APKs.
- App orchestration layer (`app`) coordinating Bluetooth state, telemetry, navigation, and settings.
- EKD01-UART protocol codec (`core/protocol`) with frame encode/decode + checksum validation.
- Bluetooth session lifecycle model (`core/bluetooth`) with discovered/connected/reconnect/disconnected states.
- Ride and navigation domain layer (`core/domain`) with reroute threshold logic and feature matrix.
- Storage abstractions (`core/storage`) with in-memory implementations for settings and ride history.

## Feature matrix (BIKEGO parity planning)

| Feature | Phase |
|---|---|
| Turn-by-turn navigation | Must-have |
| Dashboard (speed, battery, assist) | Must-have |
| Assist control | Must-have |
| Battery monitoring | Must-have |
| Ride history | Phase two |
| Offline assets | Phase two |
| Themes | Phase two |
| Notifications | Must-have |

## Project structure

- `app/` Android app module and app coordination state model.
- `core/protocol/` EKD01-UART framing and command definitions.
- `core/bluetooth/` connection session state and reconnect lifecycle model.
- `core/domain/` ride telemetry, navigation pipeline, and feature planning model.
- `core/storage/` settings and ride history repository contracts.

## Build and test

```bash
./gradlew test
./gradlew assembleRelease
```

## Build APK and publish to GitHub Release

A workflow is included at `/home/runner/work/BikeDisplayCompanion/BikeDisplayCompanion/.github/workflows/release-apk.yml`.

- Automatic release build: push a tag like `v0.1.0`.
- Manual release build: run **Build and Release APK** via GitHub Actions `workflow_dispatch` and provide `tag_name`.

The workflow generates a signing key, builds `app/build/outputs/apk/release/app-release.apk`, and uploads the signed APK to the GitHub Release.
