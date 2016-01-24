class MonetizeTransaction < ActiveRecord::Migration[5.0]
  def change
    add_monetize :transactions, :amount, currency: {present: true}
  end
end
