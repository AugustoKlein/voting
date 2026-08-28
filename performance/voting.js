import http from 'k6/http';
import { check, sleep } from 'k6';

/**
 * Teste de carga para o endpoint de votação.
 *
 * Pré-requisitos:
 * - API rodando em http://localhost:8080
 *
 * Execução:
 *   k6 run performance/vote-test.js
 */

const BASE_URL = __ENV.BASE_URL || 'http://host.docker.internal:8080';

export const options = {
    stages: [
        { duration: '10s', target: 10 },
        { duration: '20s', target: 50 },
        { duration: '20s', target: 100 },
        { duration: '10s', target: 0 },
    ],

    thresholds: {
        http_req_duration: ['p(95)<500'],
    },
};

export function setup() {
    // 1. Create pauta
    const createResponse = http.post(
        `${BASE_URL}/api/v1/pauta`,
        JSON.stringify({
            name: 'Performance Test',
            description: 'Pauta criada automaticamente pelo teste de performance',
        }),
        {
            headers: {
                'Content-Type': 'application/json',
            },
        }
    );

    check(createResponse, {
        'pauta created': (r) => r.status === 201,
    });

    if (createResponse.status !== 201) {
        throw new Error(
            `Could not create pauta. Status: ${createResponse.status}`
        );
    }

    // Location: /api/v1/pauta/{id}
    const location = createResponse.headers['Location'];
    const pautaId = location.split('/').pop();

    // 2. Open pauta
    const endsAt = new Date(
        Date.now() + 10 * 60 * 1000
    ).toISOString().slice(0, 19);

    const openResponse = http.put(
        `${BASE_URL}/api/v1/pauta/${pautaId}/open-session`,
        JSON.stringify({
            endsAt: endsAt,
        }),
        {
            headers: {
                'Content-Type': 'application/json',
            },
        }
    );

    check(openResponse, {
        'pauta opened': (r) => r.status === 200,
    });

    if (openResponse.status !== 200) {
        throw new Error(
            `Could not open pauta. Status: ${openResponse.status}`
        );
    }

    return {
        pautaId: pautaId,
    };
}

export default function (data) {
    const response = http.put(
        `${BASE_URL}/api/v1/pauta/${data.pautaId}/vote`,
        JSON.stringify({
            cpf: generateCpf(),
            votedYes: Math.random() >= 0.5,
        }),
        {
            headers: {
                'Content-Type': 'application/json',
            },
        }
    );

    check(response, {
        'vote request completed': (r) => r.status === 200 || r.status === 400 || r.status === 409,
    });
}

function generateCpf() {
    const random = Math.floor(Math.random() * 100000000000)
        .toString()
        .padStart(11, '0');

    return random;
}

/**
 * Executado ao final do teste.
 */
export function handleSummary(data) {
    const metrics = data.metrics;

    return {
        stdout: `
========================================
          RESULTADO DO TESTE
========================================

Requests:
  Total: ${metrics.http_reqs?.values?.count ?? 0}

Throughput:
  Requests/s: ${(metrics.http_reqs?.values?.rate ?? 0).toFixed(2)}

Tempo de resposta:
  Média: ${(metrics.http_req_duration?.values?.avg ?? 0).toFixed(2)} ms
  Mediana: ${(metrics.http_req_duration?.values?.med ?? 0).toFixed(2)} ms
  P90: ${(metrics.http_req_duration?.values?.['p(90)'] ?? 0).toFixed(2)} ms
  P95: ${(metrics.http_req_duration?.values?.['p(95)'] ?? 0).toFixed(2)} ms
  P99: ${(metrics.http_req_duration?.values?.['p(99)'] ?? 0).toFixed(2)} ms
  Máximo: ${(metrics.http_req_duration?.values?.max ?? 0).toFixed(2)} ms

Erros:
  Taxa de erro: ${((metrics.http_req_failed?.values?.rate ?? 0) * 100).toFixed(2)}%

========================================
`,
        'performance/results.json': JSON.stringify(data, null, 2),
    };
}
