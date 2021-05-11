const fetch = require('node-fetch');

const clientEndpoint = "http://localhost:8080/v1/pubsub-message";
const reqBody = JSON.stringify({
    "variantId": "110012",
    "seller": "SOME_SELLER"
});

const performPost = async() =>{
    let reqs=[];
    for(let i = 0; i < 10; i++) {
        const req = fetch(clientEndpoint, { method: 'POST', body: reqBody,  headers: { 'Content-Type': 'application/json' }});
        reqs.push(req);
    }

    return await Promise.all(reqs).then(res=> console.log(JSON.stringify(res))).catch(err=> console.log(JSON.stringify(err)));
}

performPost();