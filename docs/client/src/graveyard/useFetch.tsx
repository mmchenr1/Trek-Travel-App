/**
 * to eliminate a fetch error associated with the routes
 */
 import { useState, useEffect } from 'react';

 const useFetch = (url: string) => {
   const [data, setData] = useState(null);
   const [isPending, setIsPending] = useState(true);
   const [error, setError] = useState(null);
  


   //////fetch(url, { signal: abortCont.signal })
   useEffect(() => {
    console.log("before .then")
     const abortCont = new AbortController();
      fetch(url, { signal: abortCont.signal })
      .then(response => {
         console.log("enters 1st .then")
         if (!response.ok) { // error coming back from server
           throw Error('Could not load posts. Please try again.');
         } 
         return response.json();
       })
       .then(data => {
         console.log("enters 1st .then")
         setIsPending(false);
         setData(data);
         console.log(data);
         setError(null);
       })
       .catch(err => {
         if (err.name === 'AbortError') {
           console.log('fetch aborted')
         } else {
           // auto catches network / connection error
           setIsPending(false);
           setError(err.message);
         }
       })
     
  
     // abort the fetch
     return () => abortCont.abort();
   }, [url])
  
   return { data, isPending, error };
 }
   
 export default useFetch;