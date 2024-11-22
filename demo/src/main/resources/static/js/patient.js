function loadContent(page,url){
    const contentDiv = document.getElementById('content');
    let content = "";
    
    switch(page){
        case 'prescription':
            content = "";
            break;
        case 'settings':
            content = "";
            break;
        case 'profile':
            content = "";
            break;
        case 'addreport':
            content = "";
            break;
        default:
            content = "";
            break;
    }
    contentDiv.innerHTML = content;
    history.pushState({page},page,`${url}`);
}

window.onload = function(){
    var i = 0;
    var j = 0;
    while(i < window.location.pathname.length){
        if (window.location.pathname[i] == '/'){
            j = i;
        }
        ++i;
    }
    const currentPage = window.location.pathname.substring(j);
    loadContent(currentPage);
}
window.onpopstate = function (event) {
    const page = event.state?.page;
    loadContent(page);
};
