import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../state/AuthContext.jsx';
import Status from '../components/Status.jsx';

/**
 * Provides login and signup forms in a single auth screen.
 */
export default function AuthPage() {
  const [mode, setMode] = useState('login');
  const [form, setForm] = useState({ email: '', username: '', password: '' });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { login, signup } = useAuth();
  const navigate = useNavigate();

  /**
   * Keeps form state in sync with user input.
   */
  function update(field, value) {
    setForm((current) => ({ ...current, [field]: value }));
  }

  /**
   * Submits credentials to the selected auth endpoint.
   */
  async function submit(event) {
    event.preventDefault();
    setError('');
    setLoading(true);
    try {
      if (mode === 'signup') {
        await signup(form);
      } else {
        await login({ username: form.username, password: form.password });
      }
      navigate('/');
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <section className="mx-auto max-w-md">
      <div className="rounded-md border border-slate-200 bg-white p-6">
        <div className="mb-5 inline-flex rounded-md border border-slate-200 p-1">
          {['login', 'signup'].map((item) => (
            <button key={item} className={`rounded px-4 py-2 text-sm font-semibold capitalize ${mode === item ? 'bg-ink text-white' : 'text-slate-600'}`} onClick={() => setMode(item)}>
              {item}
            </button>
          ))}
        </div>
        {error && <div className="mb-4"><Status type="error">{error}</Status></div>}
        <form className="space-y-4" onSubmit={submit}>
          {mode === 'signup' && (
            <label className="block text-sm font-semibold text-slate-700">
              Email
              <input className="mt-1 w-full rounded-md border border-slate-300 px-3 py-2" type="email" value={form.email} onChange={(e) => update('email', e.target.value)} required />
            </label>
          )}
          <label className="block text-sm font-semibold text-slate-700">
            Username
            <input className="mt-1 w-full rounded-md border border-slate-300 px-3 py-2" value={form.username} onChange={(e) => update('username', e.target.value)} required />
          </label>
          <label className="block text-sm font-semibold text-slate-700">
            Password
            <input className="mt-1 w-full rounded-md border border-slate-300 px-3 py-2" type="password" value={form.password} onChange={(e) => update('password', e.target.value)} required minLength={6} />
          </label>
          <button className="w-full rounded-md bg-ember px-4 py-2 font-semibold text-white disabled:opacity-60" disabled={loading}>
            {loading ? 'Working...' : mode === 'signup' ? 'Create account' : 'Log in'}
          </button>
        </form>
      </div>
    </section>
  );
}
