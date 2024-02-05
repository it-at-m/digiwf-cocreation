# Cocreation

## Getting Started

1. Start the bpm-repo-client and API-Gateway
```bash
docker compose --profile bpm-repo-client up
```
2. Start the bpm-repo example or the bpm-serve
  - bpm-repo example with the profiles local
  - bpm-serve with the profiles local and no-security
3. Start the [bpm-modeler](https://git.muenchen.de/digitalisierung/bpm-modeler) (frontend)
  - `npm run serve`
4. Start the [digiwf-forms](https://git.muenchen.de/digitalisierung/digiwf-forms) (frontend)
  - `npm run serve`
