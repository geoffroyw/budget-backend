class AddsPaymentMeanIdToTransaction < ActiveRecord::Migration[5.0]
  def change
    add_column :transactions, :payment_mean_id, :integer, :nullable => false
  end
end
