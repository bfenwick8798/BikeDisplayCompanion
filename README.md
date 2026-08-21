# BikeDisplayCompanion

Foundation implementation for an EKD01-UART e-bike display companion app.

## Current implementation

This repository now includes a working multi-module baseline with:

- App orchestration module (`app`) coordinating Bluetooth state, telemetry, navigation, and settings.
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

- `app/` app coordination layer and state model.
- `core/protocol/` EKD01-UART framing and command definitions.
- `core/bluetooth/` connection session state and reconnect lifecycle model.
- `core/domain/` ride telemetry, navigation pipeline, and feature planning model.
- `core/storage/` settings and ride history repository contracts.

## Build and test

```bash
gradle test
```

## Next implementation steps

1. Add Android application/UI layer and foreground Bluetooth service integration.
2. Replace in-memory storage with Room/DataStore.
3. Integrate map/navigation provider and route lifecycle.
4. Add robust protocol message models for telemetry and control channels.
5. Add instrumentation tests with real or simulated EKD01-UART devices.
