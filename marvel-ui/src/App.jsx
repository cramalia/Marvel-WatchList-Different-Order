import { useEffect, useMemo, useState } from "react";

const MODES = ["RELEASE", "CHRONOLOGICAL", "ADMIN"];

export default function App() {
  const [mode, setMode] = useState("RELEASE");
  const [movies, setMovies] = useState([]);
  const [search, setSearch] = useState("");
  const [universe, setUniverse] = useState("ALL");
  const [type, setType] = useState("ALL");

  const [progress, setProgress] = useState(null);
  const [countdown, setCountdown] = useState(null);

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  async function loadMovies(currentMode = mode) {
    setLoading(true);
    setError("");
    try {
      const r = await fetch(`/movies/watch-orders?mode=${currentMode}`);
      if (!r.ok) throw new Error(`Movies HTTP ${r.status}`);
      const data = await r.json();
      setMovies(data);
    } catch (e) {
      setError(String(e));
    } finally {
      setLoading(false);
    }
  }

  async function loadStats() {
    try {
      const [p, c] = await Promise.all([
        fetch("/movies/progress").then(r => r.json()),
        fetch("/movies/countdown").then(r => r.json()),
      ]);
      setProgress(p);
      setCountdown(c);
    } catch {}
  }

  useEffect(() => {
    loadMovies(mode);
  }, [mode]);

  useEffect(() => {
    loadStats();
  }, []);

  const universes = useMemo(() => {
    const set = new Set(movies.map(m => m.universe).filter(Boolean));
    return ["ALL", ...Array.from(set)];
  }, [movies]);

  const types = useMemo(() => {
    const set = new Set(movies.map(m => m.type).filter(Boolean));
    return ["ALL", ...Array.from(set)];
  }, [movies]);

  const filtered = useMemo(() => {
    return movies.filter(m => {
      const okSearch =
        !search ||
        (m.title || "").toLowerCase().includes(search.toLowerCase());
      const okUniverse = universe === "ALL" || m.universe === universe;
      const okType = type === "ALL" || m.type === type;
      return okSearch && okUniverse && okType;
    });
  }, [movies, search, universe, type]);

  async function markWatched(movieId) {
    await fetch(`/movies/watched/${movieId}`, { method: "POST" });
    await Promise.all([loadMovies(mode), loadStats()]);
  }


  async function setRating(movieId) {
    const val = prompt("Rating 0-10 (ex: 8.5):");
    if (!val) return;
    await fetch(`/movies/watched/${movieId}/rating?value=${encodeURIComponent(val)}`, { method: "PUT" });
    await Promise.all([loadMovies(mode), loadStats()]);
  }


  return (
    <div style={styles.page}>
      <header style={styles.header}>
        <div>
          <div style={styles.kicker}>MARVEL WATCHLIST</div>
          <h1 style={styles.title}>Dashboard</h1>
        </div>

        <div style={styles.cards}>
          <div style={styles.card}>
            <div style={styles.cardLabel}>Countdown</div>
            <div style={styles.cardValue}>
              {countdown?.daysUntilDoomsday ?? "—"} days
            </div>
            <div style={styles.cardSub}>until Avengers Doomsday</div>
          </div>

          <div style={styles.card}>
            <div style={styles.cardLabel}>Progress</div>
            <div style={styles.cardValue}>
              {progress ? `${progress.watched}/${progress.total}` : "—"}
            </div>
            <div style={styles.cardSub}>
              {progress ? `${progress.percent}% watched` : "—"}
            </div>
          </div>
        </div>
      </header>

      <section style={styles.toolbar}>
        <div style={styles.row}>
          <label style={styles.label}>Order</label>
          <select style={styles.select} value={mode} onChange={(e) => setMode(e.target.value)}>
            {MODES.map(m => <option key={m} value={m}>{m}</option>)}
          </select>

          <label style={styles.label}>Universe</label>
          <select style={styles.select} value={universe} onChange={(e) => setUniverse(e.target.value)}>
            {universes.map(u => <option key={u} value={u}>{u}</option>)}
          </select>

          <label style={styles.label}>Type</label>
          <select style={styles.select} value={type} onChange={(e) => setType(e.target.value)}>
            {types.map(t => <option key={t} value={t}>{t}</option>)}
          </select>

          <input
            style={styles.search}
            placeholder="Search title..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />

          <button style={styles.btn} onClick={() => loadMovies(mode)}>Reload</button>
        </div>

        <div style={styles.meta}>
          {loading ? "Loading..." : `Showing ${filtered.length} / ${movies.length}`}
          {error ? <span style={{ color: "#ff7b7b" }}> — {error}</span> : null}
        </div>
      </section>

      <section style={styles.container}>
        <section style={styles.grid}>
          {filtered.map((m, idx) => (
            <div key={m.id} style={styles.movieCard}>
              <div style={styles.orderPill}>#{idx + 1}</div>

              <div style={styles.movieTop}>
                <div style={styles.badges}>
                  <span style={styles.badge}>{m.universe}</span>
                  <span style={styles.badge}>{m.type}</span>
                </div>
                <div style={styles.date}>{m.releaseDate ?? ""}</div>
              </div>

              <div style={styles.movieTitle}>{m.title}</div>
                  <div style={{opacity:.85, fontSize:12}}>
                     {m.watched ? "✅ Watched" : "⬜ Not watched"} {m.rating != null ? ` • ⭐ ${m.rating}` : ""}
                  </div>

              <div style={styles.actions}>
                <button style={styles.btnSmall} onClick={() => markWatched(m.id)}>Watched</button>
                <button style={styles.btnSmall} onClick={() => setRating(m.id)}>Rate</button>
              </div>
            </div>
          ))}
        </section>
      </section>
    </div>
  );
}

const styles = {
  page: { minHeight: "100vh", padding: 24, background: "#0b0f19", color: "#e9eefc", fontFamily: "system-ui" },
  container: {
    maxWidth: 1650,
    margin: "0 auto",
  },
  header: { display: "flex", justifyContent: "space-between", alignItems: "flex-start", gap: 16, marginBottom: 18 },
  kicker: { opacity: 0.75, letterSpacing: 2, fontSize: 12 },
  title: { margin: "6px 0 0", fontSize: 30 },
  cards: { display: "flex", gap: 12, flexWrap: "wrap" },
  card: { background: "#121a2a", border: "1px solid #1f2a44", borderRadius: 16, padding: 12, minWidth: 200 },
  cardLabel: { opacity: 0.8, fontSize: 12 },
  cardValue: { marginTop: 6, fontSize: 20, fontWeight: 700 },
  cardSub: { marginTop: 4, opacity: 0.8, fontSize: 12 },

  toolbar: { background: "#121a2a", border: "1px solid #1f2a44", borderRadius: 16, padding: 12, marginBottom: 16 },
  row: { display: "flex", gap: 10, alignItems: "center", flexWrap: "wrap" },
  label: { fontSize: 12, opacity: 0.8 },
  select: { padding: "8px 10px", borderRadius: 12, background: "#0f1626", color: "#e9eefc", border: "1px solid #2a3a5e" },
  search: { flex: 1, minWidth: 220, padding: "8px 10px", borderRadius: 12, background: "#0f1626", color: "#e9eefc", border: "1px solid #2a3a5e" },
  btn: { padding: "8px 12px", borderRadius: 12, background: "#0f1626", color: "#e9eefc", border: "1px solid #2a3a5e", cursor: "pointer" },
  meta: { marginTop: 8, fontSize: 12, opacity: 0.85 },

  grid: {
  display: "grid",
  gridTemplateColumns: "repeat(3, minmax(0, 1fr))",
  gap: 16,
  },
   orderPill: {
    position: "absolute",
    top: 10,
    right: 10,
    fontSize: 12,
    opacity: 0.9,
    border: "1px solid #2a3a5e",
    padding: "3px 8px",
    borderRadius: 999,
    background: "#0f1626",
  },
  movieCard: { background: "#121a2a", border: "1px solid #1f2a44", borderRadius: 16, padding: 12 },
  movieTop: { display: "flex", justifyContent: "space-between", gap: 10, marginBottom: 10 },
  badges: { display: "flex", gap: 6, flexWrap: "wrap" },
  badge: { border: "1px solid #2a3a5e", padding: "3px 8px", borderRadius: 999, fontSize: 12, opacity: 0.95 },
  date: { fontSize: 12, opacity: 0.75 },
  movieTitle: { fontSize: 16, fontWeight: 700, lineHeight: 1.2, marginBottom: 12 },
  actions: { display: "flex", gap: 8 },
  btnSmall: { padding: "8px 10px", borderRadius: 12, background: "#0f1626", color: "#e9eefc", border: "1px solid #2a3a5e", cursor: "pointer", width: "100%" },
};
