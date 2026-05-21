import { useCallback, useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { apiRequest, postJson } from '../api/client.js';
import PostCard from '../components/PostCard.jsx';
import Status from '../components/Status.jsx';
import { useAuth } from '../state/AuthContext.jsx';

/**
 * Displays one post and its comment thread.
 */
export default function PostDetailPage() {
  const { id } = useParams();
  const [post, setPost] = useState(null);
  const [comments, setComments] = useState([]);
  const [content, setContent] = useState('');
  const [status, setStatus] = useState({ loading: true, error: '' });
  const { isAuthenticated } = useAuth();

  /**
   * Loads post details and comments.
   */
  const load = useCallback(async function load() {
    setStatus({ loading: true, error: '' });
    try {
      const [nextPost, nextComments] = await Promise.all([
        apiRequest(`/posts/${id}`),
        apiRequest(`/posts/${id}/comments`)
      ]);
      setPost(nextPost);
      setComments(nextComments);
      setStatus({ loading: false, error: '' });
    } catch (err) {
      setStatus({ loading: false, error: err.message });
    }
  }, [id]);

  /**
   * Adds a comment to the current post.
   */
  async function submitComment(event) {
    event.preventDefault();
    await postJson(`/posts/${id}/comments`, { content });
    setContent('');
    await load();
  }

  useEffect(() => {
    load();
  }, [load]);

  if (status.loading) return <Status>Loading post...</Status>;
  if (status.error) return <Status type="error">{status.error}</Status>;

  return (
    <section className="space-y-4">
      <PostCard post={post} onVote={load} />
      <div className="rounded-md border border-slate-200 bg-white p-4">
        <h2 className="font-bold">Comments</h2>
        {isAuthenticated ? (
          <form className="mt-3 space-y-3" onSubmit={submitComment}>
            <textarea className="min-h-28 w-full rounded-md border border-slate-300 px-3 py-2" placeholder="Add a comment" value={content} onChange={(e) => setContent(e.target.value)} required />
            <button className="rounded-md bg-ink px-4 py-2 font-semibold text-white">Comment</button>
          </form>
        ) : (
          <p className="mt-3 text-sm text-slate-500">Log in to comment.</p>
        )}
        <div className="mt-5 space-y-3">
          {comments.length === 0 && <Status>No comments yet.</Status>}
          {comments.map((comment) => (
            <article key={comment.id} className="rounded-md border border-slate-100 p-3">
              <p className="text-xs font-semibold text-slate-500">{comment.authorUsername} • {new Date(comment.createdAt).toLocaleString()}</p>
              <p className="mt-2 whitespace-pre-wrap text-sm leading-6">{comment.content}</p>
            </article>
          ))}
        </div>
      </div>
    </section>
  );
}
