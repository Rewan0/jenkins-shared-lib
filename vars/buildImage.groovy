def call(String appDir, String image, String tag, String credId) {
    dir(appDir) {
        sh "docker build -t ${image}:${tag} ."
    }
    sh "trivy image --severity HIGH,CRITICAL ${image}:${tag}"
    withCredentials([usernamePassword(credentialsId: credId,
        usernameVariable: 'DH_USER', passwordVariable: 'DH_PASS')]) {
        sh 'echo $DH_PASS | docker login -u $DH_USER --password-stdin'
        sh "docker push ${image}:${tag}"
    }
    sh "docker rmi ${image}:${tag}"
}
