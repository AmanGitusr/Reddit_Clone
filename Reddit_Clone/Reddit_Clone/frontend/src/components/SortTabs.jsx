/**
 * Renders latest/popular feed sorting controls.
 */
export default function SortTabs({ sort, onChange }) {
  return (
    <div className="inline-flex rounded-md border border-slate-200 bg-white p-1">
      {['latest', 'popular'].map((item) => (
        <button
          key={item}
          className={`rounded px-3 py-1.5 text-sm font-semibold capitalize ${sort === item ? 'bg-ink text-white' : 'text-slate-600 hover:bg-slate-50'}`}
          onClick={() => onChange(item)}
        >
          {item}
        </button>
      ))}
    </div>
  );
}
