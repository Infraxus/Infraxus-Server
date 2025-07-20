steps {
    sh 'pip install -r requirements.txt'
    sh 'python manage.py collectstatic --noinput'
}
