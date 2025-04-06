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

function deleteMathGrade(id) {
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = `/grades/${id}/MATH`;
    document.body.appendChild(form);
    form.submit();
}

function deleteScienceGrade(id) {
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = `/grades/${id}/SCIENCE`;
    document.body.appendChild(form);
    form.submit();
}

function deleteHistoryGrade(id) {
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = `/grades/${id}/HISTORY`;
    document.body.appendChild(form);
    form.submit();
}

function deleteHistoryGrade(id) {
    window.location.href = "/grades/" + id + "/" + "history";
}

function studentInfo(id) {
    window.location.href = "/studentInformation/" + id;
}