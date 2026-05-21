import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { apiRequest, postJson } from '../api/client.js';
import Status from '../components/Status.jsx';

/**
 * Provides the authenticated post creation workflow.
 */
export default function CreatePostPage() {
  const [communities, setCommunities] = useState([]);
  const [form, setForm] = useState({ title: '', content: '', imageUrl: '', linkUrl: '', communityId: '' });
  const [error, setError] = useState('');
  const navigate = useNavigate();

  /**
   * Loads selectable communities for the post form.
   */
  async function loadCommunities() {
    try {
      const data = await apiRequest('/communities');
      setCommunities(data);
      if (data[0]) {
        setForm((current) => ({ ...current, communityId: data[0].id }));
      }
    } catch (err) {
      setError(err.message);
    }
  }

  /**
   * Updates one form field.
   */
  function update(field, value) {
    setForm((current) => ({ ...current, [field]: value }));
  }

  /**
   * Creates the post and navigates to its detail screen.
   */
  async function submit(event) {
    event.preventDefault();
    setError('');
    try {
      const post = await postJson('/posts', { ...form, communityId: Number(form.communityId) });
      navigate(`/posts/${post.id}`);
    } catch (err) {
      setError(err.message);
    }
  }

  useEffect(() => {
    loadCommunities();
  }, []);

  return (
    <section className="mx-auto max-w-2xl">
      <div className="rounded-md border border-slate-200 bg-white p-6">
        <h1 className="text-2xl font-bold">Create post</h1>
        {error && <div className="mt-4"><Status type="error">{error}</Status></div>}
        {communities.length === 0 ? (
          <div className="mt-4"><Status>Create a community before posting.</Status></div>
        ) : (
          <form className="mt-5 space-y-4" onSubmit={submit}>
            <select className="w-full rounded-md border border-slate-300 px-3 py-2" value={form.communityId} onChange={(e) => update('communityId', e.target.value)}>
              {communities.map((community) => <option key={community.id} value={community.id}>c/{community.name}</option>)}
            </select>
            <input className="w-full rounded-md border border-slate-300 px-3 py-2" placeholder="Title" value={form.title} onChange={(e) => update('title', e.target.value)} required maxLength={180} />
            <textarea className="min-h-40 w-full rounded-md border border-slate-300 px-3 py-2" placeholder="Text content" value={form.content} onChange={(e) => update('content', e.target.value)} />
            <input className="w-full rounded-md border border-slate-300 px-3 py-2" placeholder="Image URL" value={form.imageUrl} onChange={(e) => update('imageUrl', e.target.value)} />
            <input className="w-full rounded-md border border-slate-300 px-3 py-2" placeholder="Link URL" value={form.linkUrl} onChange={(e) => update('linkUrl', e.target.value)} />
            <button className="rounded-md bg-ember px-4 py-2 font-semibold text-white">Publish</button>
          </form>
        )}
      </div>
    </section>
  );
}
