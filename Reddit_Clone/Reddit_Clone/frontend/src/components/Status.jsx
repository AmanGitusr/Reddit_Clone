/**
 * Shows loading, empty, and error messages in a consistent surface.
 */
export default function Status({ type = 'info', children }) {
  const tone = type === 'error' ? 'border-red-200 bg-red-50 text-red-700' : 'border-slate-200 bg-white text-slate-600';
  return <div className={`rounded-md border px-4 py-3 text-sm ${tone}`}>{children}</div>;
}
