//import { useLocalState } from "../util/useLocalStorage";

function ajax(url, requestMethod, jwt, requestBody) {
  const fetchData = {
    headers: {
      "Content-type": "application/json",
    },
    method: requestMethod,
  };

  if (jwt) {
    fetchData.headers.Authentication = `Bearer ${jwt}`;
  }

  if (requestBody) {
    fetchData.body = JSON.stringify(requestBody);
  }

  return fetch(url, fetchData).then((response) => {
    if (response.status === 200) {
      return response.json();
    }
  });
}

export default ajax;
