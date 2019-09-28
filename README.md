# idealistic

A real estate finder. Uses the [Idealista](https://www.idealista.com) API, which is available for the ES, PT, IT markets.

## Installation

Simply clone the repo. No releases are foreseen, since this is more of a script than a library.

## Usage

```bash
export IDEALISTIC_API_KEY=...
export IDEALISTIC_API_SECRET=...
lein run -m net.vemv.idealistic.api > results_$(date +%Y%m%d%H%M)
```

Regarding the search criteria, some things are more hardcoded than others.

> As per the current default data (16 cities, 5 queries per cities), 80 API calls are used per session, plus one possibly cached one for the auth. 

## Development

The default namespace is `dev`. Under it, `(refresh)` is available, which should give you a basic "Reloaded workflow".

> It is recommended that you use `(clojure.tools.namespace.repl/refresh :after 'formatting-stack.core/format!)`.

## License

Copyright © vemv.net

This program and the accompanying materials are made available under the terms of the Eclipse Public License 2.0 which is available at http://www.eclipse.org/legal/epl-2.0.
