import React from 'react';
import { Link } from 'react-router-dom';
import userEmail from '../components/auth/Login'


interface PostProps {
  posts: any;
  title: any;
}

const PostList = ({ posts, title}: PostProps) => {
  console.log(posts['fact'])
  console.log(typeof posts)
  return (
    <div className="blog-list">
      {/* <h2>{ title }</h2>
      <p>{posts['fact']}</p> */}

      {/* <ul>
        {[ ...posts.keys()].map((key) => ( --> map
          <li key={key}> 
            {key}: {posts.get(key)}
          </li>
        ))
        }
      </ul> */}
      {/* {posts.map((post: any) => ( 
        // --> array
         <div className="blog-preview" key={post.id} >
           <Link to={`/posts/${post.description}`}>
             <h2>{ post.startDare }</h2>
             <p>Written by { post.author }</p>
           </Link>
         </div>
      ))} */}
    </div>
  );
}
 
export default PostList;



//if api response do, .then(result => result.json())
//if not I'm good ----->>> returning an map 
//