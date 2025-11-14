# qc_server.py
import json
import time
import os
import psutil
import random
from typing import Optional

from fastapi import FastAPI, HTTPException
from pydantic import BaseModel

from qiskit import QuantumCircuit
from qiskit_aer import AerSimulator


# -----------------------------------------------------
#   A RÉGI qc_workload.py TELJES LOGIKÁJA – VÁLTOZATLAN
# -----------------------------------------------------

def generate_random_circuit(qubits: int, depth: int, seed: Optional[int] = None):
    rnd = random.Random(seed)
    qc = QuantumCircuit(qubits)
    for _ in range(depth):
        q = rnd.randrange(qubits)
        gate_type = rnd.choice(["h", "x", "y", "z", "cx"])
        if gate_type == "cx" and qubits > 1:
            ctrl = q
            tgt = (q + 1) % qubits
            qc.cx(ctrl, tgt)
        else:
            getattr(qc, gate_type)(q)
    qc.measure_all(add_bits=True)
    return qc


def run_qc(qubits: int, depth: int, shots: int = 1,
           method: str = "statevector", device: Optional[str] = None,
           seed: Optional[int] = None):

    backend_opts = {"method": method}
    if device:
        backend_opts["device"] = device  # pl. "GPU"

    sim = AerSimulator(**backend_opts)
    qc = generate_random_circuit(qubits, depth, seed)

    transpiled = qc  # nem transpile-oljuk most

    proc = psutil.Process(os.getpid())
    rss_start = proc.memory_info().rss

    t0 = time.perf_counter()
    result = sim.run(transpiled, shots=shots).result()
    t1 = time.perf_counter()

    rss_end = proc.memory_info().rss
    peak_mem = max(rss_start, rss_end)

    counts = None
    try:
        counts = result.get_counts()
    except Exception:
        pass

    return {
        "qubits": qubits,
        "depth": depth,
        "shots": shots,
        "method": method,
        "device": device or "CPU",
        "runtime_sec": t1 - t0,
        "rss_bytes_end": rss_end,
        "peak_like_bytes": peak_mem,
        "success": True,
        "counts_present": counts is not None
    }


# -----------------------------------------------------
#           FASTAPI RÉSZ – ÚJ RÉSZ
# -----------------------------------------------------

app = FastAPI(title="Qiskit AER FastAPI Service")

class QCRequest(BaseModel):
    method: str
    qubits: int
    depth: int
    shots: int = 1
    device: Optional[str] = None
    seed: Optional[int] = None


@app.get("/qcproc/{method}/{qubits}/{depth}/{shots}")
def run_qc_via_get(method: str, qubits: int, depth: int, shots: int):
    """
    Ugyanaz a GET endpoint formátum, mint amit eddig a Spring hívott.
    """
    try:
        result = run_qc(
            qubits=qubits,
            depth=depth,
            shots=shots,
            method=method,
            device=None,
            seed=None
        )
        return result
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


@app.post("/qcproc")
def run_qc_via_post(req: QCRequest):
    """
    Extra: POST végpont JSON requesttel.
    """
    try:
        result = run_qc(
            qubits=req.qubits,
            depth=req.depth,
            shots=req.shots,
            method=req.method,
            device=req.device,
            seed=req.seed
        )
        return result
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


# -----------------------------------------------------
#   Lokális indító – ha közvetlenül futtatod
# -----------------------------------------------------
if __name__ == "__main__":
    import uvicorn
    uvicorn.run("qc_server:app", host="127.0.0.1", port=5001, reload=False)

