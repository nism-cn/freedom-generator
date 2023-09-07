export default {
  open(id, url) {
    let a = document.createElement("a");
    a.setAttribute("href", url);
    a.setAttribute("target", "_blank");
    a.setAttribute("id", id);
    if (!document.getElementById(id)) {
      document.body.appendChild(a);
    }
    a.click();
  },
}
