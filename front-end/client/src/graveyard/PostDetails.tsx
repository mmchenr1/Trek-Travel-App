import React from "react";
import { useParams , useHistory} from "react-router-dom";
import useFetch from "./useFetch";

const PostDetails = () => {
  const id = useParams<number>();
  const { data, error, isPending } = useFetch('http://localhost:3232' + id) ;

  const history = useHistory();

  const handleClick = () => {
    fetch('http://localhost:3232' + id, {
      method: 'DELETE'
    }).then(() => {
      history.push('/');
    }) 
  }

  return (
    <div className="post-details">
      { isPending && <div>Loading...</div> }
      { error && <div>{ error }</div> }
      {/* { data && (
        <article>
          <h2>{ data }</h2>
          <p>Written by { data }</p>
          <div>{ data }</div>
          <button onClick={handleClick}>delete</button> 
           <h2>{ data.title }</h2>
          <p>Written by { data.author }</p>
          <div>{ data.body }</div>
          <button onClick={handleClick}>delete</button>
        </article>
      )} */}
    </div>
  );
}
 
export default PostDetails;