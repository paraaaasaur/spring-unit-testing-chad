function showOrHideGrade(gradeType) {
    if (gradeType === "math") {
        const x = document.getElementById("mathGrade");
        x.style.display = x.style.display === "none" ? "block" : "none";
    }
    if (gradeType === "science") {
        const x = document.getElementById("scienceGrade");
        x.style.display = x.style.display === "none" ? "block" : "none";
    }
    if (gradeType === "history") {
        const x = document.getElementById("historyGrade");
        x.style.display = x.style.display === "none" ? "block" : "none";
    }
}

function deleteStudent(id) {
    if (false) {
        // 1. GET /delete/student/{id} via link
        window.location.href = "/delete/student/" + id;
    }

    if (false) {
        // 2. POST /delete/student/{id} via fetch API
        // - Powerful; has access to all HTTP methods
        // - Requires manual handling for full-page refresh
        fetch('http://localhost:1500/delete/student/' + id, {
            method : 'POST'
        })
            .then(res => {
                if (res.redirected) {
                    window.location.reload();
                    // window.location.href = '/';
                    // window.location.href = res.url;
                } else {
                    console.log('Response doesn\'t tell me to reload^_^');
                }
            })
            .catch(err => console.error('Error:', err))
        ;
    }

    if (true) {
        // 3. POST /delete/student/{id} via submit from dynamic form
        const form = document.createElement('form');
        form.method = 'POST';
        form.action = '/delete/student/' + id;
        document.body.appendChild(form);
        form.submit();
    }
}

function deleteMathGrade(id) {
    window.location.href = "/grades/" + id + "/" + "math";
}

function deleteScienceGrade(id) {
    window.location.href = "/grades/" + id + "/" + "science";
}

function deleteHistoryGrade(id) {
    window.location.href = "/grades/" + id + "/" + "history";
}

function studentInfo(id) {
    window.location.href = "/studentInformation/" + id;
}