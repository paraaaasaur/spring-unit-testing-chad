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
    window.location.href = "/delete/student/" + id;
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