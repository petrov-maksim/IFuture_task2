var ws;
var SEPARATOR ="#";
var clickTarget;
var mainUl = document.createElement("ul");
var contextUl;
var contextTarget;
var newNode;
var previousSelectedFolder;


function init() {
    ws = new WebSocket("ws://localhost:8080/ws");

    ws.onopen = function (event) {
        ws.send("getRoot");
    };
    ws.onmessage = function (event) {
        if (event.data.startsWith("[") && event.data.length > 5)
          onGetChildren(event.data);
        else
            addToTree(+event.data)
    };
    ws.onclose = function (event) {};

    document.addEventListener("click",resetStyle);
    setListeners();
}

function onGetChildren(data) {
    if (mainUl.firstElementChild === null)
        build(data);
    else {
        let nodes = data.substring(1, data.length - 2).split(SEPARATOR);

        let ul = document.createElement("ul");

        for (let i = 1; i < nodes.length; i++)
            addNode(nodes[i], ul);

        let parentId = nodes[0].split(" ")[0].split("=")[1];
        let parentLi;
        let lis = document.getElementsByTagName("li");
        for (let q = 0; q < lis.length; q++)
            if (lis[q].getAttribute("id") === parentId) {
                parentLi = lis[q];
                break;
            }

        if (parentLi.firstElementChild)
            parentLi.firstElementChild.remove();

        parentLi.appendChild(ul);
        parentLi.classList.remove("loadingFolder");
        parentLi.classList.add("activeFolder");
        previousSelectedFolder = parentLi;
    }
}

function onAdd() {
    if (contextTarget.getAttribute("type") === "true"){
        let name = prompt("Введите имя");
        let type = confirm("Добавить папку?");
        if (name === null || type === null || name.length === 0 || type.length === 0)
            return;

        ws.send("add" + SEPARATOR + contextTarget.getAttribute("id") + SEPARATOR + name + SEPARATOR + type);
        newNode = " name=" + name + " parentid=" + contextTarget.getAttribute("id") + " type=" + type;

        if (contextTarget.firstElementChild === null)
            contextUl = document.createElement("ul");
        else
            contextUl = contextTarget.firstElementChild;
    }
}

function addToTree(id) {
    newNode = "id=" + id + newNode;
    addNode(newNode, contextUl);
}

function onRemove() {
    ws.send("remove" + SEPARATOR + contextTarget.getAttribute("id") + SEPARATOR + contextTarget.getAttribute("type"));
    contextTarget.remove();
}

function onUpdate() {
    let newName = prompt("Введите новое имя");
    if (newName !== null && newName.length > 0) {
        ws.send("update" + SEPARATOR + contextTarget.getAttribute("id") + SEPARATOR + newName);
        contextTarget.setAttribute("name", newName);
        contextTarget.textContent = newName;

        if (contextTarget.getAttribute("type") === "true") {
            contextTarget.classList.remove("loadingFolder");
            contextTarget.classList.remove("activeFolder");
            contextTarget.classList.add("folder");
            contextTarget.setAttribute("isFirstClick","true");
        }
    }
}

function onSelect(event) {
    if (event.shiftKey)
        return;

    resetStyle();
    event.stopPropagation();
    clickTarget = event.target;

    if (clickTarget.getAttribute("type") === "false")
        return;

    if (clickTarget.getAttribute("isFirstClick") === "true") {
        ws.send("getChildren" + SEPARATOR + clickTarget.getAttribute("id"));

        clickTarget.setAttribute("isFirstClick", "false");
        clickTarget.classList.remove("folder");
        clickTarget.classList.add("loadingFolder");

        if (previousSelectedFolder) {
            previousSelectedFolder.classList.remove("loadingFolder");
            previousSelectedFolder.classList.remove("activeFolder");
            previousSelectedFolder.classList.add("folder");
        }
    }else if (clickTarget.getAttribute("isFirstClick") === "false") {
        clickTarget.classList.remove("loadingFolder");
        clickTarget.classList.remove("activeFolder");
        clickTarget.classList.add("folder");

        clickTarget.setAttribute("isFirstClick", "true");

        if (clickTarget.firstElementChild)
            clickTarget.removeChild(clickTarget.firstElementChild);
    }
}

function onRightClick(event) {
    event.preventDefault();
    contextTarget = event.target;

    let left = event.target.getBoundingClientRect().left;
    let top = event.target.getBoundingClientRect().top;
    let contextMenu = document.getElementById("contextMenu");

    contextMenu.style.left = left + 90 + "px";
    contextMenu.style.top = top - 25 + "px";
    contextMenu.classList.add("active");
}

function build(data) {
    let rootLi = document.createElement("li");
    let rootLine = data.split(" ");
    let rootId = rootLine[0].split("=")[1];
    let rootName = rootLine[1].split("=")[1].replace(/<nameSeparator>/g, " " );

    rootLi.setAttribute("id",rootId);
    rootLi.setAttribute("name", rootName);
    rootLi.setAttribute("type","true");
    rootLi.setAttribute("isFirstClick","true");
    rootLi.setAttribute("parentid","0");

    rootLi.addEventListener("click",onSelect);
    rootLi.addEventListener("contextmenu",onRightClick);

    rootLi.classList.add("folder");
    rootLi.textContent = rootName;

    mainUl.appendChild(rootLi);
    document.getElementById("treeContainer").appendChild(mainUl);
}

function addNode(node, ul) {
    let nodeLi = document.createElement("li");

    let nodeLine = node.split(" ");
    let nodeId = nodeLine[0].split("=")[1];
    let nodeName = nodeLine[1].split("=")[1].replace(/<nameSeparator>/g, " " );
    let nodeParentId = nodeLine[2].split("=")[1];
    let nodeType = nodeLine[3].split("=")[1];

    nodeLi.setAttribute("id",nodeId);
    nodeLi.setAttribute("name", nodeName);
    nodeLi.setAttribute("parentid", nodeParentId);
    nodeLi.setAttribute("type", nodeType);
    nodeLi.setAttribute("isFirstClick","true");

    nodeLi.addEventListener("click",onSelect);
    nodeLi.addEventListener('mousedown',onMouseDown);
    nodeLi.addEventListener("contextmenu",onRightClick);

    if (nodeType === "true")
        nodeLi.classList.add("folder");
    else
        nodeLi.classList.add("file");

    nodeLi.textContent = nodeName;
    ul.appendChild(nodeLi);
}

function resetStyle() {
    document.getElementById("contextMenu").classList.remove("active");
}

function setListeners() {
    document.getElementById("add").addEventListener("click",onAdd);
    document.getElementById("change").addEventListener("click",onUpdate);
    document.getElementById("remove").addEventListener("click",onRemove);
}

//-------------------------------------------Drag'n'Drop-----------------------
document.addEventListener('selectstart',onSelectStart);
function onSelectStart(event){event.preventDefault();}

function onMouseDown(event) {
    let li = event.target;
    let shiftX = event.clientX - li.getBoundingClientRect().left;
    let shiftY = event.clientY - li.getBoundingClientRect().top;
    if (event.shiftKey)
        li.addEventListener('mousemove', onMouseMove);
    else
        return;

    li.addEventListener("mouseup",onMouseUp);

    function onMouseUp(event) {
        li.hidden = true;
        let elemBelow = document.elementFromPoint(event.clientX, event.clientY);
        li.hidden = false;

        if (!elemBelow)
            return;

        if (elemBelow.getAttribute("type") === "true"){
            ws.send("move" + SEPARATOR + li.getAttribute("id") + SEPARATOR + elemBelow.getAttribute("id"));
            let ul;
            if (elemBelow.firstElementChild === null){
                ul = document.createElement("ul");
                ul.appendChild(li);
                elemBelow.appendChild(ul);
            }
            else{
                ul = elemBelow.firstElementChild;
                ul.insertBefore(li,ul.firstElementChild);
            }
        }

        li.removeEventListener('mousemove', onMouseMove);
        li.removeEventListener("mouseup",onMouseUp);
        li.style = "";

    }

    function onMouseMove(event) {
        li.style.position = 'absolute';
        li.style.zIndex = 1000;
        document.body.append(li);
        moveAt(event.pageX, event.pageY, shiftX, shiftY, li);
    }
}

function moveAt(pageX, pageY, shiftX, shiftY, elem) {
    elem.style.left = pageX - shiftX + 'px';
    elem.style.top = pageY - shiftY + 'px';
}