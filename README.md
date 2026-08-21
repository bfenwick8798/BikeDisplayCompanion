# BikeDisplayCompanion

Android companion app foundation for EKD01-UART e-bike displays.

## Current implementation

This repository now includes a first working architecture baseline with:

- Android app module (`app`) with a Compose dashboard shell.
- EKD01-UART protocol codec (`core/protocol`) with frame encode/decode + checksum validation.
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

- `app/` Android UI shell and app entrypoint.
- `core/protocol/` EKD01-UART framing and command definitions.
- `core/domain/` ride telemetry, navigation pipeline, and feature planning model.
- `core/storage/` settings and ride history repository contracts.

## Build and test

```bash
gradle test
```

## Next implementation steps

1. Replace in-memory storage with Room/DataStore.
2. Add Bluetooth transport and background service with reconnect policy.
3. Integrate map/navigation provider and route lifecycle.
4. Add robust protocol message models for telemetry and control channels.
5. Add instrumentation tests with real or simulated EKD01-UART devices.
