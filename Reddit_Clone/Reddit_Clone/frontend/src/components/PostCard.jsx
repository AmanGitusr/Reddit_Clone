import { ArrowBigDown, ArrowBigUp, MessageCircle } from 'lucide-react';
import { Link } from 'react-router-dom';
import { postJson } from '../api/client.js';
import { useAuth } from '../state/AuthContext.jsx';

/**
 * Renders a post summary with voting actions and metadata.
 */
export default function PostCard({ post, onVote }) {
  const { isAuthenticated } = useAuth();

  /**
   * Sends a vote and notifies the parent so it can refresh feed state.
   */
  async function vote(type) {
    if (!isAuthenticated) {
      alert('Please log in to vote.');
      return;
    }
    await postJson('/votes', { postId: post.id, type });
    onVote?.();
  }

  return (
    <article className="grid grid-cols-[56px_1fr] rounded-md border border-slate-200 bg-white">
      <div className="flex flex-col items-center gap-1 border-r border-slate-100 bg-slate-50 py-3">
        <button className="text-slate-500 hover:text-ember" onClick={() => vote('UPVOTE')} title="Upvote">
          <ArrowBigUp className="h-6 w-6" />
        </button>
        <strong className="text-sm">{post.score}</strong>
        <button className="text-slate-500 hover:text-blue-600" onClick={() => vote('DOWNVOTE')} title="Downvote">
          <ArrowBigDown className="h-6 w-6" />
        </button>
      </div>
      <div className="min-w-0 p-4">
        <p className="text-xs text-slate-500">
          <Link className="font-semibold text-slate-700 hover:underline" to={`/c/${post.communitySlug}`}>
            c/{post.communityName}
          </Link>{' '}
          posted by {post.authorUsername} • {new Date(post.createdAt).toLocaleString()}
        </p>
        <Link to={`/posts/${post.id}`} className="mt-1 block text-lg font-semibold text-ink hover:underline">
          {post.title}
        </Link>
        {post.imageUrl && <img className="mt-3 max-h-80 rounded-md object-cover" src={post.imageUrl} alt={post.title} />}
        {post.content && <p className="mt-3 whitespace-pre-wrap text-sm leading-6 text-slate-700">{post.content}</p>}
        {post.linkUrl && (
          <a className="mt-3 block break-words text-sm font-medium text-blue-700 hover:underline" href={post.linkUrl} target="_blank" rel="noreferrer">
            {post.linkUrl}
          </a>
        )}
        <Link to={`/posts/${post.id}`} className="mt-4 inline-flex items-center gap-2 text-sm font-semibold text-slate-600 hover:text-ink">
          <MessageCircle className="h-4 w-4" /> {post.commentCount} comments
        </Link>
      </div>
    </article>
  );
}
