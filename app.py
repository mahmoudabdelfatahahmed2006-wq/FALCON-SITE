from flask import Flask, redirect, url_for, render_template, session
from database import db

from routes.auth import auth
from routes.products import products
from routes.orders import orders
from routes.reports import reports
from models import User, Product, Order, OrderItem

app = Flask(__name__)

app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///store.db'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
app.config['SECRET_KEY'] = 'supersecretkey'

db.init_app(app)

app.register_blueprint(auth)
app.register_blueprint(products)
app.register_blueprint(orders)
app.register_blueprint(reports)


with app.app_context():
    db.create_all()

   
    if not User.query.filter_by(username='admin').first():
        admin = User(username='admin', password='1234', role='Admin')
        db.session.add(admin)
        db.session.commit()


@app.route('/')
def home():
    return redirect(url_for('auth.login'))


@app.route('/dashboard')
def dashboard():
    if 'user_id' not in session:
        return redirect(url_for('auth.login'))

    total_products = Product.query.count()
    total_orders = Order.query.count()
    low_stock = Product.query.filter(Product.quantity <= 5).all()

    total_sales = db.session.query(db.func.sum(Order.total_price)).scalar() or 0

    return render_template(
        'dashboard.html',
        total_products=total_products,
        total_orders=total_orders,
        low_stock=low_stock,
        total_sales=total_sales
    )


if __name__ == '__main__':
    app.run(debug=True)